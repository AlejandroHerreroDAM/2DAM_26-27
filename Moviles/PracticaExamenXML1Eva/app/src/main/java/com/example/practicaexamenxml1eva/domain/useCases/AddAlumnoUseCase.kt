package com.example.practicaexamenxml1eva.domain.useCases

import com.example.practicaexamenxml1eva.data.Alumnos
import com.example.practicaexamenxml1eva.domain.model.Alumno

class AddAlumnoUseCase {
    // addAlumno guarda el Alumno creado desde el formulario.
    fun addAlumno(alumno: Alumno) = Alumnos.addAlumno(alumno)
}