package com.example.practicacompose_alejandroherrero.domain.useCases

import com.example.practicacompose_alejandroherrero.data.Alumnos
import com.example.practicacompose_alejandroherrero.domain.model.Alumno

class DeleteAlumnoUseCase {
    fun deleteAlumno(a: Alumno) = Alumnos.deleteAlumno(a)
}