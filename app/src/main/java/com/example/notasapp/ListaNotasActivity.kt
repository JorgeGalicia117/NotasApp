package com.example.notasapp

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notasapp.model.Estado
import com.example.notasapp.viewmodel.ListaNotasViewModel

class ListaNotasActivity : AppCompatActivity() {

    private val viewModel: ListaNotasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_notas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rvNotas = findViewById<RecyclerView>(R.id.rv_notas)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val tvEstado = findViewById<TextView>(R.id.tv_estado)

        val adapter = NotaAdapter()
        rvNotas.layoutManager = LinearLayoutManager(this)
        rvNotas.adapter = adapter

        viewModel.estadoNotas.observe(this) { estado ->
            when (estado) {
                is Estado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    tvEstado.visibility = View.GONE
                }
                is Estado.Exito -> {
                    progress.visibility = View.GONE
                    adapter.actualizarNotas(estado.datos)
                    if (estado.datos.isEmpty()) {
                        tvEstado.text = "Aún no tienes notas guardadas"
                        tvEstado.visibility = View.VISIBLE
                    } else {
                        tvEstado.visibility = View.GONE
                    }
                }
                is Estado.Error -> {
                    progress.visibility = View.GONE
                    tvEstado.text = estado.mensaje
                    tvEstado.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargarNotas()
    }
}