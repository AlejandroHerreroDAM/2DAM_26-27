package com.example.practicaexamencompose1eva.ui.main

import com.example.practicaexamencompose1eva.domain.model.Alumno

data class MainState(
    val alumnos: List<Alumno> = emptyList(),
    val alumnoSeleccionado: Alumno? = null,
    val atributosSeleccionado: List<Pair<String, String>> = emptyList(),
    val error: String? = null
)