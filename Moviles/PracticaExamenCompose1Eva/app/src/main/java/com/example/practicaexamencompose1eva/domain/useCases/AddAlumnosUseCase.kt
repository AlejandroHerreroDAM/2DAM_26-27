package com.example.practicaexamencompose1eva.domain.useCases

import com.example.practicaexamencompose1eva.data.Alumnos
import com.example.practicaexamencompose1eva.domain.model.Alumno

class AddAlumnoUseCase {
    fun addAlumno(alumno: Alumno) = Alumnos.addAlumno(alumno)
}