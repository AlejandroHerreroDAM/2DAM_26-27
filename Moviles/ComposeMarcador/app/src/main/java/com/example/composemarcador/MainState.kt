package com.example.composemarcador

data class MainState(
    val tiempo: String = "10:00",
    val periodo: String = "1.º",
    val nombreEquipo1: String = "Equipo1",
    val nombreEquipo2: String = "Equipo2",
    val puntosEquipo1: Int = 0,
    val puntosEquipo2: Int = 0,
    val faltasEquipo1: Int = 0,
    val faltasEquipo2: Int = 0,
    val modoEditar: Boolean = false,
    val error : String? = null,
    )
