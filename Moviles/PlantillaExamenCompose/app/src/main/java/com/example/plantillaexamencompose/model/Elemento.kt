package com.example.plantillaexamencompose.model

/**
 * CLASE MODELO (Entidad de negocio)
 * Representa cada objeto individual que vamos a guardar en la lista.
 * En el examen, adapta estos campos según lo que pida el enunciado (nombre, precio, cantidad, etc.).
 * Es importante tener un 'id' único para que LazyColumn identifique cada fila sin errores.
 */
data class Elemento(
    val id: Int,
    val nombre: String
)