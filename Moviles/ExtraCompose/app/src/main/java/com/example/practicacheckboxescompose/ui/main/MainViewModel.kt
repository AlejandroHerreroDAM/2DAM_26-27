package com.example.practicacheckboxescompose.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practicacheckboxescompose.domain.model.Tarea
import com.example.practicacheckboxescompose.domain.useCases.AddTareaUseCase
import com.example.practicacheckboxescompose.domain.useCases.CargarTareasUseCase
import com.example.practicacheckboxescompose.domain.useCases.DelTareasUseCase
import com.example.practicacheckboxescompose.domain.useCases.UpdateTareaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    private val cargarTareas: CargarTareasUseCase,
    private val addTarea: AddTareaUseCase,
    private val delTareas: DelTareasUseCase,
    private val updateTarea: UpdateTareaUseCase
) : ViewModel() {
    // StateFlow expone el estado de forma observable a MainScreen mediante collectAsState().
    private val _state = MutableStateFlow(MainState(tareas = cargarTareas()))
    val state: StateFlow<MainState> = _state.asStateFlow()

    // Recarga el almacen y permite modificar tambien otros campos del estado.
    private fun refresh(transform: (MainState) -> MainState = { it }) {
        _state.value = transform(_state.value.copy(tareas = cargarTareas()))
    }

    // Marca o desmarca una tarea individual en el conjunto idsMarcados.
    fun handleToggleCheck(id: Int) {
        val selected = _state.value.idsMarcados.toMutableSet()
        if (!selected.add(id)) selected.remove(id)
        _state.value = _state.value.copy(idsMarcados = selected, error = null)
    }

    // Selecciona todas las tareas visibles o deselecciona solo esas tareas.
    fun handleMarcarODesmarcarTodas() {
        val visibles = _state.value.tareasFiltradas.map { it.id }.toSet()
        if (visibles.isEmpty()) return
        val selected = _state.value.idsMarcados
        val allSelected = visibles.all { it in selected }
        val newSelection = if (allSelected) selected - visibles else selected + visibles
        _state.value = _state.value.copy(idsMarcados = newSelection)
    }

    // Borra las seleccionadas y deja visible un mensaje si no habia seleccion.
    fun handleBorrarMarcadas() {
        val ids = _state.value.idsMarcados
        if (ids.isEmpty()) {
            _state.value = _state.value.copy(error = "Selecciona al menos una tarea para borrar")
            return
        }
        delTareas(ids)
        refresh { it.copy(idsMarcados = emptySet(), error = null) }
    }

    // Cambia a completadas todas las tareas seleccionadas en una sola operacion.
    fun handleCompletarMarcadas() {
        val ids = _state.value.idsMarcados
        if (ids.isEmpty()) return
        updateTarea.marcarCompletadas(ids, true)
        refresh { it.copy(idsMarcados = emptySet(), error = null) }
    }

    // Invierte el estado completada/pendiente de una tarjeta concreta.
    fun handleToggleCompletada(tarea: Tarea) {
        updateTarea(tarea.copy(completada = !tarea.completada))
        refresh()
    }

    // Guarda la tarea que debe abrir el dialogo; null significa cerrarlo.
    fun handleAbrirEdicion(tarea: Tarea?) {
        _state.value = _state.value.copy(tareaEnEdicion = tarea, error = null)
    }

    // Elimina el título de la tarea seleccionada.
    fun handleEliminarTituloEdicion() {
        val tarea = _state.value.tareaEnEdicion ?: return
        val actualizada = tarea.copy(titulo = "")
        updateTarea(actualizada)
        refresh { it.copy(tareaEnEdicion = actualizada, error = null) }
    }

    // Elimina la categoría de la tarea seleccionada.
    fun handleEliminarCategoriaEdicion() {
        val tarea = _state.value.tareaEnEdicion ?: return
        val actualizada = tarea.copy(categoria = "")
        updateTarea(actualizada)
        refresh { it.copy(tareaEnEdicion = actualizada, error = null) }
    }

    // Pasa el estado de la tarea seleccionada a pendiente.
    fun handleEliminarEstadoEdicion() {
        val tarea = _state.value.tareaEnEdicion ?: return
        val actualizada = tarea.copy(completada = false)
        updateTarea(actualizada)
        refresh { it.copy(tareaEnEdicion = actualizada, error = null) }
    }

    // Guarda los cambios de edicion y cierra el dialogo.
    fun handleGuardarEdicion(tareaEditada: Tarea) {
        updateTarea(tareaEditada)
        refresh { it.copy(tareaEnEdicion = null, error = null) }
    }

    // Solo anade una tarea si el titulo contiene texto despues de quitar espacios.
    fun handleAdd(titulo: String, categoria: String) {
        val nombre = titulo.trim()
        if (nombre.isBlank()) return
        addTarea(Tarea(titulo = nombre, categoria = categoria))
        refresh()
    }

    // Actualiza el texto que MainState utiliza para filtrar titulo y categoria.
    fun handleBusquedaChange(texto: String) {
        _state.value = _state.value.copy(textoBusqueda = texto, error = null)
    }

    // Cambia el filtro mostrado mediante los FilterChip.
    fun handleFiltroChange(filtro: FiltroTarea) {
        _state.value = _state.value.copy(filtroEstado = filtro)
    }
}

// La fabrica permite pasar manualmente los casos de uso al crear el ViewModel.
class MainViewModelFactory(
    private val cargarTareas: CargarTareasUseCase,
    private val addTarea: AddTareaUseCase,
    private val delTareas: DelTareasUseCase,
    private val updateTarea: UpdateTareaUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(cargarTareas, addTarea, delTareas, updateTarea) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
