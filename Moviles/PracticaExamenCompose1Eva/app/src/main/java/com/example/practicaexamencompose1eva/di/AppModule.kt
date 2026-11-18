package com.example.practicaexamencompose1eva.di

import com.example.practicaexamencompose1eva.domain.useCases.AddAlumnoUseCase
import com.example.practicaexamencompose1eva.domain.useCases.CargarAlumnosUseCase
import com.example.practicaexamencompose1eva.domain.useCases.DelAlumnoUseCase
import com.example.practicaexamencompose1eva.domain.useCases.UpdateAlumnoUseCase

object AppModule {
    val cargarAlumnosUseCase: CargarAlumnosUseCase = CargarAlumnosUseCase()
    val addAlumnoUseCase: AddAlumnoUseCase = AddAlumnoUseCase()
    val delAlumnoUseCase: DelAlumnoUseCase = DelAlumnoUseCase()
    val updateAlumnoUseCase: UpdateAlumnoUseCase = UpdateAlumnoUseCase()
}