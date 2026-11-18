package com.example.practicaexamenxml1eva.domain.useCases

import com.example.practicaexamenxml1eva.data.Alumnos
import com.example.practicaexamenxml1eva.domain.model.Alumno

class DelAlumnoUseCase {
    // delAlumno elimina la fila seleccionada del RecyclerView.
    fun delAlumno(alumno: Alumno) = Alumnos.deleteAlumno(alumno)
}