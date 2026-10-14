package com.example.mvvm_ejercicio_alejandro.ui.main

import com.example.mvvm_ejercicio_alejandro.domain.model.Coche

data class MainState(
    val listaCoches: List<Coche> = emptyList(),
    val textoPantalla: String = "Haz click",
    val error: String? = null,
)
