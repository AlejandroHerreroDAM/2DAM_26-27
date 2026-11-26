package com.example.practicaexamenxml1eva.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practicaexamenxml1eva.domain.model.Alumno
import com.example.practicaexamenxml1eva.domain.useCases.AddAlumnoUseCase
import com.example.practicaexamenxml1eva.domain.useCases.CargarAlumnosUseCase
import com.example.practicaexamenxml1eva.domain.useCases.DelAlumnoUseCase
import com.example.practicaexamenxml1eva.domain.useCases.UpdateAlumnoUseCase

class MainViewModel(
    private val addAlumnoUseCase: AddAlumnoUseCase,
    private val delAlumnoUseCase: DelAlumnoUseCase,
    private val cargarAlumnosUseCase: CargarAlumnosUseCase,
    private val updateAlumnoUseCase: UpdateAlumnoUseCase,
) : ViewModel() {

    // _state es el estado interno; state lo observa MainActivity.
    private val _state = MutableLiveData(MainState())
    val state: LiveData<MainState> = _state

    init {
        handleCargaLista()
    }

    fun handleCargaLista() {
        _state.value = _state.value?.copy(alumnos = cargarAlumnosUseCase.cargarLista())
    }

    fun handleSeleccionar(alumno: Alumno) {
        // nuevoSeleccionado cambia al pulsar; si se repite, se deselecciona.
        val seleccionadoActual = _state.value?.alumnoSeleccionado
        val nuevoSeleccionado = if (seleccionadoActual?.id == alumno.id) null else alumno
        _state.value = _state.value?.copy(
            alumnoSeleccionado = nuevoSeleccionado,
            atributosSeleccionado = nuevoSeleccionado?.obtenerAtributos() ?: emptyList()
        )
    }

    fun handleEliminarAtributo(clave: String) {
        // actual es el alumno seleccionado en el segundo RecyclerView.
        val actual = _state.value?.alumnoSeleccionado ?: return

        // modificado contiene el alumno tras eliminar el atributo elegido.
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
        _state.value = _state.value?.copy(
            alumnos = cargarAlumnosUseCase.cargarLista(),
            alumnoSeleccionado = modificado,
            atributosSeleccionado = modificado.obtenerAtributos()
        )
    }

    fun handleAdd(alumno: Alumno) {
        try {
            addAlumnoUseCase.addAlumno(alumno)
            _state.value = _state.value?.copy(
                alumnos = cargarAlumnosUseCase.cargarLista(),
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value?.copy(error = e.message)
        }
    }

    fun handleDelete() {
        val seleccionado = _state.value?.alumnoSeleccionado
        if (seleccionado == null) {
            _state.value = _state.value?.copy(error = "Selecciona un alumno para poder borrarlo")
            return
        }
        try {
            delAlumnoUseCase.delAlumno(seleccionado)
            _state.value = _state.value?.copy(
                alumnos = cargarAlumnosUseCase.cargarLista(),
                alumnoSeleccionado = null,
                atributosSeleccionado = emptyList(),
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value?.copy(error = e.message)
        }
    }

    fun handleErrorMostrado() {
        _state.value = _state.value?.copy(error = null)
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