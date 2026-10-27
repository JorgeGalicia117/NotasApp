package com.example.notasapp.local

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("sesion_notasapp", Context.MODE_PRIVATE)

    fun guardarSesion(token: String, nombre: String) {
        prefs.edit()
            .putString("token", token)
            .putString("nombre", nombre)
            .apply()
    }

    fun obtenerToken(): String? = prefs.getString("token", null)

    fun obtenerNombre(): String? = prefs.getString("nombre", null)

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }
}