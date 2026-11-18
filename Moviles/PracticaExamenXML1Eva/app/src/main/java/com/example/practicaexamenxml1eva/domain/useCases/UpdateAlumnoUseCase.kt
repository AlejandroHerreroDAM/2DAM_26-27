package com.example.practicaexamenxml1eva.domain.useCases

import com.example.practicaexamenxml1eva.data.Alumnos
import com.example.practicaexamenxml1eva.domain.model.Alumno

class UpdateAlumnoUseCase {
    // updateAlumno guarda cambios hechos desde rvAtributos.
    fun updateAlumno(alumno: Alumno) = Alumnos.updateAlumno(alumno)
}