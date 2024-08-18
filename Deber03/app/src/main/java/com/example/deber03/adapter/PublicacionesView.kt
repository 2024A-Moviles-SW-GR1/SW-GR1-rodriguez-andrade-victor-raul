package com.example.deber03.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deber03.Publicaciones
import com.example.deber03.R

class PublicacionesView (view: View):RecyclerView.ViewHolder(view){

    val foto = view.findViewById<ImageView>(R.id.im_usuario)
    val usuario = view.findViewById<TextView>(R.id.nom_user)
    val puesto= view.findViewById<TextView>(R.id.num_empleo)
    val descripcion = view.findViewById<TextView>(R.id.tv_header)
    val tiempo = view.findViewById<TextView>(R.id.tiempo)
    val estadoSeguir = view.findViewById<TextView>(R.id.seguir)
    val fotoPublicidad = view.findViewById<ImageView>(R.id.im_publicacion)


    //Acciones de la publicacion

    val recomendar: ImageButton = itemView.findViewById(R.id.btn_recomendar)
    val comentar: ImageButton = itemView.findViewById(R.id.btn_comentar)
    val compartir: ImageButton = itemView.findViewById(R.id.btn_compartir)
    val enviar: ImageButton = itemView.findViewById(R.id.btn_enviar)
    fun render(publicaciones:Publicaciones){
        usuario.text=publicaciones.usuario
        puesto.text=publicaciones.puesto
        descripcion.text=publicaciones.descripcion
        tiempo.text=publicaciones.tiempo
        estadoSeguir.text=publicaciones.seguir

        //Imagenes
        Glide.with(foto.context).load(publicaciones.foto).into(foto)
        Glide.with(fotoPublicidad).load(publicaciones.fotoPublicidad).into(fotoPublicidad)

        //LLamado de acciones

        // Configura las acciones
        recomendar.setOnClickListener {
            // Acción para recomendar la publicación
            // Aquí puedes implementar la lógica para recomendar, por ejemplo, aumentar el contador de likes
            Toast.makeText(itemView.context, "Recomendaste la publicación de ${publicaciones.usuario}", Toast.LENGTH_SHORT).show()
        }

        comentar.setOnClickListener {
            // Acción para comentar la publicación
            // Aquí puedes abrir un diálogo para añadir un comentario, por ejemplo
            Toast.makeText(itemView.context, "Comentaste en la publicación de ${publicaciones.usuario}", Toast.LENGTH_SHORT).show()
        }

        compartir.setOnClickListener {
            // Acción para compartir la publicación
            // Podrías abrir un intent para compartir el contenido en redes sociales
            Toast.makeText(itemView.context, "Compartiste la publicación de ${publicaciones.usuario}", Toast.LENGTH_SHORT).show()
        }

        enviar.setOnClickListener {
            // Acción para enviar la publicación
            // Aquí podrías abrir un diálogo para enviar el contenido a otro usuario
            Toast.makeText(itemView.context, "Enviaste la publicación de ${publicaciones.usuario}", Toast.LENGTH_SHORT).show()
        }
    }
}