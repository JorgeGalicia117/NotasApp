package com.example.notasapp.repository

import android.content.Context
import com.example.notasapp.local.SessionManager
import com.example.notasapp.model.Estado
import com.example.notasapp.model.LoginRequest
import com.example.notasapp.model.LoginResponse
import com.example.notasapp.model.Nota
import com.example.notasapp.model.NotaRequest
import com.example.notasapp.network.RetrofitClient
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

class NotasRepository(context: Context) {

    private val api = RetrofitClient.crearApi(context)
    private val session = SessionManager(context)

    suspend fun login(email: String, password: String): Estado<LoginResponse> {
        val resultado = ejecutar { api.login(LoginRequest(email, password)) }
        if (resultado is Estado.Exito) {
            session.guardarSesion(resultado.datos.token, resultado.datos.usuario.nombre)
        }
        return resultado
    }

    suspend fun crearNota(titulo: String, contenido: String): Estado<Nota> {
        return ejecutar { api.crearNota(NotaRequest(titulo, contenido)) }
    }

    suspend fun obtenerNotas(): Estado<List<Nota>> {
        return ejecutar { api.obtenerNotas() }
    }

    fun obtenerNombreUsuario(): String {
        return session.obtenerNombre() ?: "Usuario"
    }

    fun cerrarSesion() {
        session.cerrarSesion()
    }

    private suspend fun <T> ejecutar(llamada: suspend () -> Response<T>): Estado<T> {
        return try {
            val respuesta = llamada()
            val cuerpo = respuesta.body()
            if (respuesta.isSuccessful && cuerpo != null) {
                Estado.Exito(cuerpo)
            } else {
                Estado.Error(leerMensajeError(respuesta.errorBody()?.string(), respuesta.code()))
            }
        } catch (e: IOException) {
            Estado.Error("No se pudo conectar con el servidor. Revisa tu conexión")
        } catch (e: Exception) {
            Estado.Error("Ocurrió un error inesperado")
        }
    }

    private fun leerMensajeError(cuerpo: String?, codigo: Int): String {
        return try {
            JSONObject(cuerpo ?: "").getString("mensaje")
        } catch (e: Exception) {
            "Error del servidor, código $codigo"
        }
    }
}