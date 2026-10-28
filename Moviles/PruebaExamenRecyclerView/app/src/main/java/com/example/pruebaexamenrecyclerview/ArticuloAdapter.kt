package com.example.pruebaexamenrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebaexamenrecyclerview.databinding.ItemInventarioBinding

// ADAPTADOR: Su único trabajo es coger la lista de 'Articulos' y dibujarla en las filas 'item_inventario.xml'.
class ArticuloAdapter (
    val onClick: (Articulo) -> Unit // Lambda para avisar a la Activity de que han tocado una fila
) : ListAdapter<Articulo, ArticuloAdapter.ArticuloViewHolder>(ArticuloDiffCallback()) {

    // 1. INFLAR: Convierte el diseño XML (item_inventario.xml) en un objeto real de la pantalla.
    // Solo se llama para crear las primeras filas visibles.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val binding = ItemInventarioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ArticuloViewHolder(binding, onClick)
    }

    // 2. RECICLAR: Coge una fila ya creada y le mete los datos del artículo que toca pintar ahora.
    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // EL MOLDE DE LA FILA: Conecta un objeto 'Articulo' con los TextViews físicos del XML.
    class ArticuloViewHolder(
        private val binding: ItemInventarioBinding,
        private val onClickDelete : (Articulo) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(articulo: Articulo) {
            with(binding) {
                tvNombreArticulo.text = articulo.nombre // Pinta el dato

                root.setOnClickListener {
                    onClickDelete(articulo) // Avisa hacia fuera si tocan la fila entera
                }
            }
        }
    }
}

// LAS REGLAS: Le enseñan al Adapter a comparar listas para hacer animaciones de añadir/borrar solas.
class ArticuloDiffCallback : DiffUtil.ItemCallback<Articulo>() {
    override fun areItemsTheSame(oldItem: Articulo, newItem: Articulo): Boolean =
        oldItem.nombre == newItem.nombre

    override fun areContentsTheSame(oldItem: Articulo, newItem: Articulo): Boolean =
        oldItem == newItem
}