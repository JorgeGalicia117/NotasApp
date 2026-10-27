package com.example.notasapp.model

sealed class Estado<out T> {
    data object Cargando : Estado<Nothing>()
    data class Exito<T>(val datos: T) : Estado<T>()
    data class Error(val mensaje: String) : Estado<Nothing>()
}