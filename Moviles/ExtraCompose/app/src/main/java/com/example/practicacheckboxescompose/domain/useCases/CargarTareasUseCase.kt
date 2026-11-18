package com.example.practicacheckboxescompose.domain.useCases

import com.example.practicacheckboxescompose.data.Tareas

class CargarTareasUseCase {
    // operator fun invoke permite ejecutar el caso de uso como cargarTareas().
    operator fun invoke() = Tareas.getTareas()
}
