package com.example.practicacompose_alejandroherrero.domain.useCases

import com.example.practicacompose_alejandroherrero.data.Alumnos

class CargarAlumnosUseCase {
    fun cargarAlumnos() = Alumnos.getListaAlumnos()
}