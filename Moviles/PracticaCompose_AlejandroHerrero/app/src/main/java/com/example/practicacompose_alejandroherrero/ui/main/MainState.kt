package com.example.practicacompose_alejandroherrero.ui.main

import com.example.practicacompose_alejandroherrero.domain.model.Alumno

data class MainState(
    val alumnos: List<Alumno> = emptyList(),
    val alumno: Alumno = Alumno(),
    val alumnoSeleccionado: Alumno? = null,
    val mensaje: String? = null,
    val error: String? = null,
)

