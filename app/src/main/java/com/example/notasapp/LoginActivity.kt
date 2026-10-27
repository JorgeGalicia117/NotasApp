package com.example.notasapp

import android.content.Intent
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
import com.example.notasapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val tvError = findViewById<TextView>(R.id.tv_error)

        btnLogin.setOnClickListener {
            viewModel.iniciarSesion(etEmail.text.toString(), etPassword.text.toString())
        }

        viewModel.estadoLogin.observe(this) { estado ->
            when (estado) {
                is Estado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    tvError.visibility = View.GONE
                    btnLogin.isEnabled = false
                }
                is Estado.Exito -> {
                    progress.visibility = View.GONE
                    btnLogin.isEnabled = true
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
                is Estado.Error -> {
                    progress.visibility = View.GONE
                    btnLogin.isEnabled = true
                    tvError.text = estado.mensaje
                    tvError.visibility = View.VISIBLE
                }
            }
        }
    }
}