package com.example.practicacheckboxescompose.domain.useCases

import com.example.practicacheckboxescompose.data.Tareas

class DelTareasUseCase {
    // Recibe los ids seleccionados y delega el borrado masivo al almacen.
    operator fun invoke(ids: Set<Int>) = Tareas.deleteTareasByIds(ids)
}
