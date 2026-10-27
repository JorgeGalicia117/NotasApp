package com.example.notasapp.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class Usuario(
    val id: Int,
    val nombre: String,
    val email: String
)

data class LoginResponse(
    val token: String,
    val usuario: Usuario
)

data class NotaRequest(
    val titulo: String,
    val contenido: String
)

data class Nota(
    val id: Int,
    @SerializedName("usuario_id") val usuarioId: Int,
    val titulo: String,
    val contenido: String,
    @SerializedName("fecha_creacion") val fechaCreacion: String
)