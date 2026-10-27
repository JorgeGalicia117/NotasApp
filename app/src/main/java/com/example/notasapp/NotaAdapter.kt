package com.example.notasapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notasapp.model.Nota

class NotaAdapter : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    private var notas: List<Nota> = emptyList()

    fun actualizarNotas(nuevasNotas: List<Nota>) {
        notas = nuevasNotas
        notifyDataSetChanged()
    }

    class NotaViewHolder(vista: View) : RecyclerView.ViewHolder(vista) {
        val titulo: TextView = vista.findViewById(R.id.tv_item_titulo)
        val contenido: TextView = vista.findViewById(R.id.tv_item_contenido)
        val fecha: TextView = vista.findViewById(R.id.tv_item_fecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = notas[position]
        holder.titulo.text = nota.titulo
        holder.contenido.text = nota.contenido
        holder.fecha.text = nota.fechaCreacion
    }

    override fun getItemCount(): Int = notas.size
}