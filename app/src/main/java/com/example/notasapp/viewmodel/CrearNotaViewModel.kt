package com.example.notasapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notasapp.model.Estado
import com.example.notasapp.model.Nota
import com.example.notasapp.repository.NotasRepository
import kotlinx.coroutines.launch

class CrearNotaViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = NotasRepository(application)

    private val _estadoCrear = MutableLiveData<Estado<Nota>>()
    val estadoCrear: LiveData<Estado<Nota>> = _estadoCrear

    fun guardarNota(titulo: String, contenido: String) {
        if (titulo.isBlank() || contenido.isBlank()) {
            _estadoCrear.value = Estado.Error("Escribe un título y un contenido")
            return
        }
        _estadoCrear.value = Estado.Cargando
        viewModelScope.launch {
            _estadoCrear.value = repositorio.crearNota(titulo.trim(), contenido.trim())
        }
    }
}