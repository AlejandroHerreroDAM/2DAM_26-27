package com.example.practicaexamenxml1eva.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.practicaexamenxml1eva.databinding.ItemAtributoBinding

class AtributoAdapter(
    // onAtributoClick devuelve la clave de la fila pulsada de rvAtributos.
    private val onAtributoClick: (String) -> Unit
) : ListAdapter<Pair<String, String>, AtributoAdapter.AtributoViewHolder>(AtributoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtributoViewHolder {
        val binding = ItemAtributoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AtributoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AtributoViewHolder, position: Int) {
        // position indica el atributo que ocupa esta fila.
        holder.bind(getItem(position))
    }

    inner class AtributoViewHolder(
        private val binding: ItemAtributoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(atributo: Pair<String, String>) {
            // clave es el nombre y valor es el dato mostrado en tvNombreAtributo.
            val (clave, valor) = atributo
            binding.tvNombreAtributo.text = "$clave: $valor"
            binding.cardAtributo.setOnClickListener {
                onAtributoClick(clave)
            }
        }
    }

    class AtributoDiffCallback : DiffUtil.ItemCallback<Pair<String, String>>() {
        override fun areItemsTheSame(oldItem: Pair<String, String>, newItem: Pair<String, String>): Boolean {
            return oldItem.first == newItem.first
        }

        override fun areContentsTheSame(oldItem: Pair<String, String>, newItem: Pair<String, String>): Boolean {
            return oldItem == newItem
        }
    }
}