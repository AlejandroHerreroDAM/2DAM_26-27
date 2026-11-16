package com.example.practicacompose_alejandroherrero.di

import com.example.practicacompose_alejandroherrero.domain.useCases.AddAlumnoUseCase
import com.example.practicacompose_alejandroherrero.domain.useCases.CargarAlumnosUseCase
import com.example.practicacompose_alejandroherrero.domain.useCases.DeleteAlumnoUseCase

object AppModule {
    val addAlumnoUseCase: AddAlumnoUseCase = AddAlumnoUseCase()
    val deleteAlumnoUseCase: DeleteAlumnoUseCase = DeleteAlumnoUseCase()
    val cargarAlumnosUseCase: CargarAlumnosUseCase = CargarAlumnosUseCase()
}