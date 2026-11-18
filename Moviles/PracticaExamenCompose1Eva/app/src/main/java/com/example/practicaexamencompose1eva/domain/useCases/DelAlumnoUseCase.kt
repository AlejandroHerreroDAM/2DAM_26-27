package com.example.practicaexamencompose1eva.domain.useCases

import com.example.practicaexamencompose1eva.data.Alumnos
import com.example.practicaexamencompose1eva.domain.model.Alumno

class DelAlumnoUseCase {
    fun delAlumno(alumno: Alumno) = Alumnos.deleteAlumno(alumno)
}