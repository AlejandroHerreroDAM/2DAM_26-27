package com.example.pruebaexamenrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebaexamenrecyclerview.databinding.ItemInventarioBinding

class ArticuloAdapter (
    val onClick: (Articulo) -> Unit
) : ListAdapter<Articulo, ArticuloAdapter.ArticuloViewHolder>(ArticuloDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticuloViewHolder {
        val binding = ItemInventarioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticuloViewHolder(binding,onClick)
    }

    override fun onBindViewHolder(
        holder: ArticuloViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class ArticuloViewHolder(
        private val binding: ItemInventarioBinding,
        private val onClickDelete : (Articulo) -> Unit,

        ) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(articulo: Articulo) {

            with(binding) {
                tvNombreArticulo.text = articulo.nombre

                root.setOnClickListener {

                    onClickDelete(articulo)
                }
            }
        }

    }


}

class ArticuloDiffCallback : DiffUtil.ItemCallback<Articulo>() {
    override fun areItemsTheSame(oldItem: Articulo, newItem: Articulo): Boolean =
        oldItem.nombre == newItem.nombre

    override fun areContentsTheSame(oldItem: Articulo, newItem: Articulo): Boolean =
        oldItem == newItem
}