package com.example.notasapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notasapp.viewmodel.DashboardViewModel

class DashboardActivity : AppCompatActivity() {

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvBienvenida = findViewById<TextView>(R.id.tv_bienvenida)
        val btnCrearNota = findViewById<Button>(R.id.btn_crear_nota)
        val btnVerNotas = findViewById<Button>(R.id.btn_ver_notas)
        val btnCerrarSesion = findViewById<Button>(R.id.btn_cerrar_sesion)

        viewModel.nombre.observe(this) { nombre ->
            tvBienvenida.text = "Bienvenido, $nombre"
        }

        btnCrearNota.setOnClickListener {
            startActivity(Intent(this, CrearNotaActivity::class.java))
        }

        btnVerNotas.setOnClickListener {
            startActivity(Intent(this, ListaNotasActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            viewModel.cerrarSesion()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}