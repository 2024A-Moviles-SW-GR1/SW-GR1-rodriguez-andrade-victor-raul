package com.example.deber03.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deber03.Notificaciones
import com.example.deber03.R

class NotificacionAdapter(private val notificacionesList: List<Notificaciones>)
    : RecyclerView.Adapter<NotificacionView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionView {
        val layoutInflater= LayoutInflater.from(parent.context)
        return NotificacionView(layoutInflater.inflate(R.layout.item_notificaciones,parent,false))
    }

    override fun onBindViewHolder(holder: NotificacionView, position: Int) {
        val item=notificacionesList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = notificacionesList.size

}