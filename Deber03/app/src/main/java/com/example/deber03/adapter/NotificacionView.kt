package com.example.deber03.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deber03.Notificaciones
import com.example.deber03.R

class NotificacionView(view:View): RecyclerView.ViewHolder(view) {
    val img_user=view.findViewById<ImageView>(R.id.im_usuarionotif)
    val usuarionotif=view.findViewById<TextView>(R.id.nombre_usuario)
    val accion = view.findViewById<TextView>(R.id.text_accion)
    val contenido = view.findViewById<TextView>(R.id.contenido)
    val tiemponotf=view.findViewById<TextView>(R.id.tiempoNotif)

    //Boton
    val mas:ImageButton=itemView.findViewById(R.id.btn_mas)

    fun render(notificaciones: Notificaciones){
        usuarionotif.text=notificaciones.usuario
        accion.text=notificaciones.accionNotificacion
        contenido.text=notificaciones.contenido
        tiemponotf.text=notificaciones.tiempoNotif

        Glide.with(img_user.context).load(notificaciones.fotoUsuario).into(img_user)

        mas.setOnClickListener {  }
    }
}