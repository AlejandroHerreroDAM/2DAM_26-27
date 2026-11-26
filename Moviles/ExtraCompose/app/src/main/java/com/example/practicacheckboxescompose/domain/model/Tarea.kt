package com.example.practicacheckboxescompose.domain.model

// Modelo de dominio: representa los datos basicos de una tarea.
data class Tarea(
    val id: Int = 0,
    val titulo: String,
    val categoria: String,
    val completada: Boolean = false
)
