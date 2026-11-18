package com.example.practicaexamenxml1eva.ui

import com.example.practicaexamenxml1eva.domain.model.Alumno

data class MainState(
    // alumnos alimenta el RecyclerView principal listado.
    val alumnos: List<Alumno> = emptyList(),
    // alumnoSeleccionado guarda la fila pulsada.
    val alumnoSeleccionado: Alumno? = null,
    // atributosSeleccionado alimenta el RecyclerView rvAtributos.
    val atributosSeleccionado: List<Pair<String, String>> = emptyList(),
    val error: String? = null
)