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

class ListaNotasViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = NotasRepository(application)

    private val _estadoNotas = MutableLiveData<Estado<List<Nota>>>()
    val estadoNotas: LiveData<Estado<List<Nota>>> = _estadoNotas

    fun cargarNotas() {
        _estadoNotas.value = Estado.Cargando
        viewModelScope.launch {
            _estadoNotas.value = repositorio.obtenerNotas()
        }
    }
}