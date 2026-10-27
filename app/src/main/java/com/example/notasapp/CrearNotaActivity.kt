package com.example.notasapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notasapp.model.Estado
import com.example.notasapp.viewmodel.CrearNotaViewModel

class CrearNotaActivity : AppCompatActivity() {

    private val viewModel: CrearNotaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_nota)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etTitulo = findViewById<EditText>(R.id.et_titulo)
        val etContenido = findViewById<EditText>(R.id.et_contenido)
        val btnGuardar = findViewById<Button>(R.id.btn_guardar)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val tvMensaje = findViewById<TextView>(R.id.tv_mensaje)

        btnGuardar.setOnClickListener {
            viewModel.guardarNota(etTitulo.text.toString(), etContenido.text.toString())
        }

        viewModel.estadoCrear.observe(this) { estado ->
            when (estado) {
                is Estado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    tvMensaje.visibility = View.GONE
                    btnGuardar.isEnabled = false
                }
                is Estado.Exito -> {
                    progress.visibility = View.GONE
                    btnGuardar.isEnabled = true
                    tvMensaje.text = "Nota guardada correctamente"
                    tvMensaje.setTextColor(Color.parseColor("#2E7D32"))
                    tvMensaje.visibility = View.VISIBLE
                    etTitulo.text.clear()
                    etContenido.text.clear()
                }
                is Estado.Error -> {
                    progress.visibility = View.GONE
                    btnGuardar.isEnabled = true
                    tvMensaje.text = estado.mensaje
                    tvMensaje.setTextColor(Color.parseColor("#C62828"))
                    tvMensaje.visibility = View.VISIBLE
                }
            }
        }
    }
}