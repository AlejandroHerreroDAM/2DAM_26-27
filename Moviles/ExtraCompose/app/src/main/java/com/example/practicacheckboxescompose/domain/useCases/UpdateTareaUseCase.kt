package com.example.practicacheckboxescompose.domain.useCases

import com.example.practicacheckboxescompose.data.Tareas
import com.example.practicacheckboxescompose.domain.model.Tarea

class UpdateTareaUseCase {
    // Actualiza una tarea concreta con los datos editados.
    operator fun invoke(tarea: Tarea) = Tareas.updateTarea(tarea)
    fun marcarCompletadas(ids: Set<Int>, completada: Boolean) =
        Tareas.marcarCompletadasByIds(ids, completada)
}
