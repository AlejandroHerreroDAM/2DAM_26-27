package com.example.practicacompose_alejandroherrero.domain.useCases

import com.example.practicacompose_alejandroherrero.data.Alumnos
import com.example.practicacompose_alejandroherrero.domain.model.Alumno

class AddAlumnoUseCase {
    fun addAlumno(a: Alumno) = Alumnos.addAlumno(a)
}