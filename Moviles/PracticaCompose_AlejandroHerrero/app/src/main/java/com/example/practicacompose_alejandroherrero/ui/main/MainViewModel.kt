package com.example.practicacompose_alejandroherrero.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import com.example.practicacompose_alejandroherrero.domain.useCases.AddAlumnoUseCase

import com.example.practicacompose_alejandroherrero.domain.useCases.CargarAlumnosUseCase
import com.example.practicacompose_alejandroherrero.domain.useCases.DeleteAlumnoUseCase

import com.example.practicacompose_alejandroherrero.domain.model.Alumno
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val addAlumnoUseCase: AddAlumnoUseCase,
    private val deleteAlumnoUseCase: DeleteAlumnoUseCase,
    private val cargarAlumnosUseCase: CargarAlumnosUseCase
): ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val listaInicial = cargarAlumnosUseCase.cargarAlumnos()
            _state.update { it.copy(alumnos = listaInicial) }
        }
    }

    fun onChangeAlumno(alumnoForm: Alumno) {
        _state.update {
            it.copy(
                alumno = alumnoForm,
                error = null,
                mensaje = null
            )
        }
    }

    fun onAddAlumno() {
        viewModelScope.launch {
            val form = _state.value.alumno

            if (form.nombre.isBlank()) {
                _state.update { it.copy(error = "El nombre es obligatorio") }
                return@launch
            }

            addAlumnoUseCase.addAlumno(form)

            val listaActualizada = cargarAlumnosUseCase.cargarAlumnos()
            _state.update {
                it.copy(
                    alumnos = listaActualizada,

                    error = null,
                    mensaje = "Alumno añadido correctamente"
                )
            }
        }
    }

    fun onSelectAlumno(alumno: Alumno) {
        _state.update {
            it.copy(
                alumnoSeleccionado = alumno,
                error = null,
                mensaje = "Alumno ${alumno.nombre} seleccionado"
            )
        }
    }

    fun onDeleteAlumno() {
        val seleccionado = _state.value.alumnoSeleccionado
        if (seleccionado == null) {
            _state.update {
                it.copy(error = "Debes seleccionar un alumno")
            }
            return
        }

        viewModelScope.launch {
            deleteAlumnoUseCase.deleteAlumno(seleccionado)
            val listaActualizada = cargarAlumnosUseCase.cargarAlumnos()
            _state.update {
                it.copy(
                    alumnos = listaActualizada,
                    alumnoSeleccionado = null,
                    error = null,
                    mensaje = "Alumno eliminado correctamente"
                )
            }
        }
    }

    fun clearMessages() {
        _state.update { it.copy(error = null, mensaje = null) }
    }

}

class MainViewModelFactory(
    private val addAlumnoUseCase: AddAlumnoUseCase,
    private val deleteAlumnoUseCase: DeleteAlumnoUseCase,
    private val cargarAlumnosUseCase: CargarAlumnosUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                addAlumnoUseCase,
                deleteAlumnoUseCase,
                cargarAlumnosUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}