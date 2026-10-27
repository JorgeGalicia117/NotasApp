package com.example.notasapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notasapp.repository.NotasRepository

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = NotasRepository(application)

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    init {
        _nombre.value = repositorio.obtenerNombreUsuario()
    }

    fun cerrarSesion() {
        repositorio.cerrarSesion()
    }
}