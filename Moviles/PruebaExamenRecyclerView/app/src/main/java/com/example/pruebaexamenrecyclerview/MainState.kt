package com.example.pruebaexamenrecyclerview

// ESTADO: La "fotografía" actual de tu pantalla.
// Todo lo que cambie visualmente en la Activity debe estar aquí guardado.
data class MainState(
    val articulos: List<Articulo> = emptyList(),
    val articuloSeleccionado: String = "Cargando Inventario...",
    val error: String? = null,
)