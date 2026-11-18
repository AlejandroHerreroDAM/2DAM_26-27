package com.example.practicacheckboxescompose.data

import com.example.practicacheckboxescompose.domain.model.Tarea

object Tareas {
    // Almacen de ejemplo: la app usa memoria, por lo que los cambios duran mientras el proceso siga activo.
    // Se separa de la interfaz para que ViewModel no acceda directamente a la lista mutable.
    private val tareas = mutableListOf(
        Tarea(1, "Preparar presentación", "Trabajo"),
        Tarea(2, "Repasar Kotlin", "Estudios"),
        Tarea(3, "Comprar alimentos", "Personal"),
        Tarea(4, "Entregar práctica", "Urgente", completada = true),
        Tarea(5, "Planificar vacaciones", "Personal"),
        Tarea(6, "Revisar documentación", "Trabajo", completada = true)
    )
    private var nextId = 7

    // Devuelve una copia de la lista para evitar que componentes externos la modifiquen.
    fun getTareas(): List<Tarea> = tareas.toList()
    // nextId genera un identificador distinto y el copy evita modificar el objeto recibido.
    fun addTarea(tarea: Tarea) { tareas.add(tarea.copy(id = nextId++)) }
    // removeAll elimina todas las tareas cuyo id aparezca en el conjunto seleccionado.
    fun deleteTareasByIds(ids: Set<Int>) { tareas.removeAll { it.id in ids } }
    // Busca por id y reemplaza la tarea completa con los nuevos datos.
    fun updateTarea(tareaActualizada: Tarea) {
        val index = tareas.indexOfFirst { it.id == tareaActualizada.id }
        if (index >= 0) tareas[index] = tareaActualizada
    }
    // Actualiza varias tareas a la vez sin crear una operacion separada para cada una.
    fun marcarCompletadasByIds(ids: Set<Int>, completada: Boolean) {
        tareas.replaceAll { tarea ->
            if (tarea.id in ids) tarea.copy(completada = completada) else tarea
        }
    }
}
