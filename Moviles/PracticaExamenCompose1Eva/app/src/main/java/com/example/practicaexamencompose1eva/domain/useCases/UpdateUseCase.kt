package com.example.practicaexamencompose1eva.domain.useCases

import com.example.practicaexamencompose1eva.data.Alumnos
import com.example.practicaexamencompose1eva.domain.model.Alumno

class UpdateAlumnoUseCase {
    fun updateAlumno(alumno: Alumno) = Alumnos.updateAlumno(alumno)
}