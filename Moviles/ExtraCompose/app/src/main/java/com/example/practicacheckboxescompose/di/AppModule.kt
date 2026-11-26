package com.example.practicacheckboxescompose.di

import com.example.practicacheckboxescompose.domain.useCases.AddTareaUseCase
import com.example.practicacheckboxescompose.domain.useCases.CargarTareasUseCase
import com.example.practicacheckboxescompose.domain.useCases.DelTareasUseCase
import com.example.practicacheckboxescompose.domain.useCases.UpdateTareaUseCase

object AppModule {
    // Contenedor manual: crea una sola instancia de cada caso de uso.
    val cargarTareasUseCase = CargarTareasUseCase()
    val addTareaUseCase = AddTareaUseCase()
    val delTareasUseCase = DelTareasUseCase()
    val updateTareaUseCase = UpdateTareaUseCase()
}
