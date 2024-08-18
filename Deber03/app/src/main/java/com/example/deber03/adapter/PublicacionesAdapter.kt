package com.example.deber03.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deber03.Publicaciones
import com.example.deber03.R

class PublicacionesAdapter(private val publicacionesList: List<Publicaciones>): RecyclerView.Adapter<PublicacionesView>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionesView {
        val layoutInflater= LayoutInflater.from(parent.context)
        return PublicacionesView(layoutInflater.inflate(R.layout.item_publicaciones, parent,false))
    }

    override fun getItemCount(): Int = publicacionesList.size


    override fun onBindViewHolder(holder: PublicacionesView, position: Int) {
        val item = publicacionesList[position]
        holder.render(item)
    }

}