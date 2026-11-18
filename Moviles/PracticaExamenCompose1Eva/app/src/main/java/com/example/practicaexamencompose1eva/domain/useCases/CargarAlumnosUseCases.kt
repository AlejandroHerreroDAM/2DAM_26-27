package com.example.practicaexamencompose1eva.domain.useCases

import com.example.practicaexamencompose1eva.data.Alumnos

class CargarAlumnosUseCase {
    fun cargarLista() = Alumnos.getAlumnos()
}