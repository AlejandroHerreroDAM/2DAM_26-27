// Declara el paquete de modelos del dominio.
package com.example.mvvm_ejercicio_alejandro.domain.model

// Define un modelo inmutable de datos para representar un coche.
data class Coche(
    // Guarda la marca del coche.
    val marca: String,
    // Guarda el modelo concreto del coche.
    val modelo: String,
    // Guarda la matrícula representada como número entero.
    val matricula: Int,
)
