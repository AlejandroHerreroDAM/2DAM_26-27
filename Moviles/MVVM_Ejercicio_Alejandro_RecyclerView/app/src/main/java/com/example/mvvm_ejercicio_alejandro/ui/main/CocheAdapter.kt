package com.example.mvvm_ejercicio_alejandro.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_ejercicio_alejandro.databinding.ItemCocheBinding
import com.example.mvvm_ejercicio_alejandro.domain.model.Coche

class CocheAdapter(
    private val onClickDelete: (Coche) -> Unit
) : ListAdapter<Coche, CocheAdapter.CocheViewHolder>(CocheDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocheViewHolder {
        val binding = ItemCocheBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CocheViewHolder(binding, onClickDelete)
    }

    override fun onBindViewHolder(holder: CocheViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CocheViewHolder(
        private val binding: ItemCocheBinding,
        private val onClickDelete: (Coche) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(coche: Coche) {
            binding.tvCoche.text = "${coche.marca} ${coche.modelo} (${coche.matricula})"
            binding.root.setOnClickListener { onClickDelete(coche) }
        }
    }
}

class CocheDiffCallback : DiffUtil.ItemCallback<Coche>() {
    override fun areItemsTheSame(oldItem: Coche, newItem: Coche): Boolean =
        oldItem.matricula == newItem.matricula

    override fun areContentsTheSame(oldItem: Coche, newItem: Coche): Boolean =
        oldItem == newItem
}
