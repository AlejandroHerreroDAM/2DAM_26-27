package com.example.practicacheckboxescompose.domain.useCases

import com.example.practicacheckboxescompose.data.Tareas
import com.example.practicacheckboxescompose.domain.model.Tarea

class AddTareaUseCase {
    // Encapsula la operacion de alta sin exponer directamente el almacén Tareas.
    operator fun invoke(tarea: Tarea) = Tareas.addTarea(tarea)
}
