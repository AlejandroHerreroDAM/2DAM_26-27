package com.example.pruebaexamenrecyclerview

data class MainState(
    val articulos: List<Articulo> = emptyList(),
    val articuloSeleccionado: String = "Cargando Inventario...",
    val error: String? = null,
)
