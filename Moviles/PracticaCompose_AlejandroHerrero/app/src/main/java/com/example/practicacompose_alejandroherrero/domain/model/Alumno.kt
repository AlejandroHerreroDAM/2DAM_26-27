package com.example.practicacompose_alejandroherrero.domain.model

data class Alumno(
    val id: Long = 0L,
    val nombre: String = "",
    val apellido: String = "",
    val curso: String ="",
    val edad: Int = 0,
    val correo: String ="",
    val activo: Boolean = true,
)