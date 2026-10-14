// Declara el paquete de la pantalla principal.
package com.example.mvvm_ejercicio_alejandro.ui.main

// Importa el modelo que se almacenará en el estado.
import com.example.mvvm_ejercicio_alejandro.domain.model.Coche

// Representa todos los datos que la interfaz necesita mostrar.
data class MainState(
    // Guarda la lista actual de coches.
    val listaCoches: List<Coche> = emptyList(),
    // Guarda el texto que se presenta para el coche aleatorio.
    val textoPantalla: String = "Haz click",
    // Guarda un mensaje de error opcional.
    val error: String? = null,
)
