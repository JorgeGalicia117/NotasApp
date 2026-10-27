package com.example.notasapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notasapp.model.Estado
import com.example.notasapp.model.LoginResponse
import com.example.notasapp.repository.NotasRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = NotasRepository(application)

    private val _estadoLogin = MutableLiveData<Estado<LoginResponse>>()
    val estadoLogin: LiveData<Estado<LoginResponse>> = _estadoLogin

    fun iniciarSesion(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _estadoLogin.value = Estado.Error("Completa el email y la contraseña")
            return
        }
        _estadoLogin.value = Estado.Cargando
        viewModelScope.launch {
            _estadoLogin.value = repositorio.login(email.trim(), password)
        }
    }
}