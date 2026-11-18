package com.example.practicaexamencompose1eva.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practicaexamencompose1eva.domain.model.Alumno
import com.example.practicaexamencompose1eva.domain.useCases.AddAlumnoUseCase
import com.example.practicaexamencompose1eva.domain.useCases.CargarAlumnosUseCase
import com.example.practicaexamencompose1eva.domain.useCases.DelAlumnoUseCase
import com.example.practicaexamencompose1eva.domain.useCases.UpdateAlumnoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val addAlumnoUseCase: AddAlumnoUseCase,
    private val delAlumnoUseCase: DelAlumnoUseCase,
    private val cargarAlumnosUseCase: CargarAlumnosUseCase,
    private val updateAlumnoUseCase: UpdateAlumnoUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        handleCargaLista()
    }

    fun handleCargaLista() {
        _state.update { it.copy(alumnos = cargarAlumnosUseCase.cargarLista()) }
    }

    fun handleSeleccionar(alumno: Alumno) {
        val seleccionadoActual = _state.value.alumnoSeleccionado
        val nuevoSeleccionado = if (seleccionadoActual?.id == alumno.id) null else alumno
        _state.update {
            it.copy(
                alumnoSeleccionado = nuevoSeleccionado,
                atributosSeleccionado = nuevoSeleccionado?.obtenerAtributos() ?: emptyList()
            )
        }
    }

    fun handleEliminarAtributo(clave: String) {
        val actual = _state.value.alumnoSeleccionado ?: return

        val modificado = when (clave) {
            "Nombre" -> actual.copy(nombre = "")
            "Apellidos" -> actual.copy(apellidos = "")
            "Curso" -> actual.copy(curso = "")
            "Edad" -> actual.copy(edad = 0)
            "Email" -> actual.copy(email = "")
            "Estado" -> actual.copy(activo = false)
            else -> actual
        }

        updateAlumnoUseCase.updateAlumno(modificado)
        _state.update {
            it.copy(
                alumnos = cargarAlumnosUseCase.cargarLista(),
                alumnoSeleccionado = modificado,
                atributosSeleccionado = modificado.obtenerAtributos()
            )
        }
    }

    fun handleAdd(alumno: Alumno) {
        try {
            addAlumnoUseCase.addAlumno(alumno)
            _state.update {
                it.copy(
                    alumnos = cargarAlumnosUseCase.cargarLista(),
                    error = null
                )
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }

    fun handleDelete() {
        val seleccionado = _state.value.alumnoSeleccionado
        if (seleccionado == null) {
            _state.update { it.copy(error = "Selecciona un alumno para poder borrarlo") }
            return
        }
        try {
            delAlumnoUseCase.delAlumno(seleccionado)
            _state.update {
                it.copy(
                    alumnos = cargarAlumnosUseCase.cargarLista(),
                    alumnoSeleccionado = null,
                    atributosSeleccionado = emptyList(),
                    error = null
                )
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }

    fun handleErrorMostrado() {
        _state.update { it.copy(error = null) }
    }
}

class MainViewModelFactory(
    private val cargarAlumnosUseCase: CargarAlumnosUseCase,
    private val addAlumnoUseCase: AddAlumnoUseCase,
    private val delAlumnoUseCase: DelAlumnoUseCase,
    private val updateAlumnoUseCase: UpdateAlumnoUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                addAlumnoUseCase,
                delAlumnoUseCase,
                cargarAlumnosUseCase,
                updateAlumnoUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}