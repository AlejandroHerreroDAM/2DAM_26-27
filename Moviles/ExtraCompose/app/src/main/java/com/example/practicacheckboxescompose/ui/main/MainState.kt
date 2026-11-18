package com.example.practicacheckboxescompose.ui.main

import com.example.practicacheckboxescompose.domain.model.Tarea

// Opciones disponibles para filtrar la lista por estado de finalizacion.
enum class FiltroTarea { TODAS, PENDIENTES, COMPLETADAS }

// Contiene toda la informacion que la interfaz necesita para renderizarse.
data class MainState(
    val tareas: List<Tarea> = emptyList(),
    val idsMarcados: Set<Int> = emptySet(),
    val textoBusqueda: String = "",
    val filtroEstado: FiltroTarea = FiltroTarea.TODAS,
    val tareaEnEdicion: Tarea? = null,
    val error: String? = null
) {
    // La UI recibe siempre esta lista ya filtrada por texto y por estado.
    val tareasFiltradas: List<Tarea>
        get() = tareas.filter { tarea ->
            val coincideTexto = textoBusqueda.isBlank() ||
                tarea.titulo.contains(textoBusqueda, ignoreCase = true) ||
                tarea.categoria.contains(textoBusqueda, ignoreCase = true)
            val coincideEstado = when (filtroEstado) {
                FiltroTarea.TODAS -> true
                FiltroTarea.PENDIENTES -> !tarea.completada
                FiltroTarea.COMPLETADAS -> tarea.completada
            }
            coincideTexto && coincideEstado
        }
}
