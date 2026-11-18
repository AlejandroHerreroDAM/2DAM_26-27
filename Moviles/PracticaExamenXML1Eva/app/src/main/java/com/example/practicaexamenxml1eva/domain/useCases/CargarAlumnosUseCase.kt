package com.example.practicaexamenxml1eva.domain.useCases

import com.example.practicaexamenxml1eva.data.Alumnos
import com.example.practicaexamenxml1eva.domain.model.Alumno

class CargarAlumnosUseCase {

    // cargarLista obtiene las filas que verá el RecyclerView listado.
    fun cargarLista() = Alumnos.getAlumnos()

}