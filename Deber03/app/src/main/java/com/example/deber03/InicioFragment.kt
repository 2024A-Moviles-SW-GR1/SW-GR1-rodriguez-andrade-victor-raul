package com.example.deber03

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deber03.adapter.PublicacionesAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InicioFragment : Fragment() {


    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Configura los elementos de la UI, como el RecyclerView
        initRecyclerView(view)
    }
    fun initRecyclerView(view: View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerInicio)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter=PublicacionesAdapter(ContenidoPublicaciones.publicacionesList)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            InicioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}