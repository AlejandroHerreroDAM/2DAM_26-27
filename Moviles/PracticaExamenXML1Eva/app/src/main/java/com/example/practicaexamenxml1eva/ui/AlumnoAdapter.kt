package com.example.practicaexamenxml1eva.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.practicaexamenxml1eva.databinding.ItemAlumnoBinding
import com.example.practicaexamenxml1eva.domain.model.Alumno
import com.google.android.material.color.MaterialColors

class AlumnoAdapter(
    // onAlumnoClick devuelve la fila pulsada del RecyclerView listado.
    private val onAlumnoClick: (Alumno) -> Unit
) : ListAdapter<Alumno, AlumnoAdapter.AlumnoViewHolder>(AlumnoDiffCallback()) {

    // alumnoSeleccionadoId coincide con la tarjeta seleccionada.
    private var alumnoSeleccionadoId: Int? = null

    fun setAlumnoSeleccionado(id: Int?) {
        // anteriorId permite actualizar solo la tarjeta anterior y la actual.
        val anteriorId = alumnoSeleccionadoId
        alumnoSeleccionadoId = id

        // Refrescamos solo la tarjeta anterior y la nueva para optimizar
        currentList.forEachIndexed { index, alumno ->
            if (alumno.id == anteriorId || alumno.id == id) {
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val binding = ItemAlumnoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlumnoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        // position indica qué elemento de la lista ocupa esta fila.
        holder.bind(getItem(position))
    }

    inner class AlumnoViewHolder(
        private val binding: ItemAlumnoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(alumno: Alumno) {
            // Las variables tv... muestran los datos del alumno en cada fila.
            binding.tvIniciales.text = alumno.obtenerIniciales()
            binding.tvNombreAlumno.text = alumno.nombre
            binding.tvApellidosAlumno.text = alumno.apellidos
            binding.tvCursoAlumno.text = alumno.curso
            binding.tvEdadAlumno.text = "${alumno.edad} años"
            binding.tvCorreoAlumno.text = alumno.email

            // cardEstado cambia de color según alumno.activo.
            if (alumno.activo) {
                binding.tvEstadoAlumno.text = "Activo"
                val bgColor = MaterialColors.getColor(
                    binding.cardEstado,
                    com.google.android.material.R.attr.colorTertiaryContainer
                )
                val textColor = MaterialColors.getColor(
                    binding.tvEstadoAlumno,
                    com.google.android.material.R.attr.colorOnTertiaryContainer
                )
                binding.cardEstado.setCardBackgroundColor(bgColor)
                binding.tvEstadoAlumno.setTextColor(textColor)
            } else {
                binding.tvEstadoAlumno.text = "Inactivo"
                val bgColor = MaterialColors.getColor(
                    binding.cardEstado,
                    com.google.android.material.R.attr.colorErrorContainer
                )
                val textColor = MaterialColors.getColor(
                    binding.tvEstadoAlumno,
                    com.google.android.material.R.attr.colorOnErrorContainer
                )
                binding.cardEstado.setCardBackgroundColor(bgColor)
                binding.tvEstadoAlumno.setTextColor(textColor)
            }

            // Resalta cardAlumno si su id coincide con alumnoSeleccionadoId.
            val estaSeleccionado = (alumno.id == alumnoSeleccionadoId)
            val density = binding.root.resources.displayMetrics.density

            if (estaSeleccionado) {
                val strokeColor = MaterialColors.getColor(
                    binding.cardAlumno,
                    androidx.appcompat.R.attr.colorPrimary
                )
                val bgCardColor = MaterialColors.getColor(
                    binding.cardAlumno,
                    com.google.android.material.R.attr.colorPrimaryContainer
                )
                binding.cardAlumno.strokeColor = strokeColor
                binding.cardAlumno.strokeWidth = (2 * density).toInt()
                binding.cardAlumno.setCardBackgroundColor(bgCardColor)
            } else {
                val strokeColor = MaterialColors.getColor(
                    binding.cardAlumno,
                    com.google.android.material.R.attr.colorOutlineVariant
                )
                val bgCardColor = MaterialColors.getColor(
                    binding.cardAlumno,
                    com.google.android.material.R.attr.colorSurface
                )
                binding.cardAlumno.strokeColor = strokeColor
                binding.cardAlumno.strokeWidth = (1 * density).toInt()
                binding.cardAlumno.setCardBackgroundColor(bgCardColor)
            }

            // Al pulsar cardAlumno se devuelve el alumno seleccionado.
            binding.cardAlumno.setOnClickListener {
                onAlumnoClick(alumno)
            }
        }
    }

    class AlumnoDiffCallback : DiffUtil.ItemCallback<Alumno>() {
        override fun areItemsTheSame(oldItem: Alumno, newItem: Alumno): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Alumno, newItem: Alumno): Boolean {
            return oldItem == newItem
        }
    }
}