package com.example.practicaexamenxml1eva.di

import com.example.practicaexamenxml1eva.domain.useCases.AddAlumnoUseCase
import com.example.practicaexamenxml1eva.domain.useCases.CargarAlumnosUseCase
import com.example.practicaexamenxml1eva.domain.useCases.DelAlumnoUseCase
import com.example.practicaexamenxml1eva.domain.useCases.UpdateAlumnoUseCase

object AppModule {

    // Instancias reutilizadas por MainViewModel para acceder a los datos.
    val cargarAlumnosUseCase: CargarAlumnosUseCase = CargarAlumnosUseCase()
    val addAlumnoUseCase: AddAlumnoUseCase = AddAlumnoUseCase()
    val delAlumnoUseCase: DelAlumnoUseCase = DelAlumnoUseCase()
    val updateAlumnoUseCase: UpdateAlumnoUseCase = UpdateAlumnoUseCase()
}