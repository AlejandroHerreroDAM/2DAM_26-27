package com.example.composemarcador

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    fun onToggleEditar() {
        _state.update { estadoActual ->
            estadoActual.copy(
                modoEditar = !estadoActual.modoEditar
            )
        }
    }

    fun onSumarPuntosEquipo1(puntos: Int) {
        _state.update { estadoActual ->
            val nuevosPuntos = if (estadoActual.modoEditar) {
                (estadoActual.puntosEquipo1 - puntos).coerceAtLeast(0)
            } else {
                estadoActual.puntosEquipo1 + puntos
            }
            estadoActual.copy(puntosEquipo1 = nuevosPuntos)
        }
    }

    fun onSumarPuntosEquipo2(puntos: Int) {
        _state.update { estadoActual ->
            val nuevosPuntos = if (estadoActual.modoEditar) {
                (estadoActual.puntosEquipo2 - puntos).coerceAtLeast(0)
            } else {
                estadoActual.puntosEquipo2 + puntos
            }
            estadoActual.copy(puntosEquipo2 = nuevosPuntos)
        }
    }

    fun onSumarFaltaEquipo1() {
        _state.update { estadoActual ->
            val nuevasFaltas = if (estadoActual.modoEditar) {
                (estadoActual.faltasEquipo1 - 1).coerceAtLeast(0)
            } else {
                estadoActual.faltasEquipo1 + 1
            }
            estadoActual.copy(faltasEquipo1 = nuevasFaltas)
        }
    }

    fun onSumarFaltaEquipo2() {
        _state.update { estadoActual ->
            val nuevasFaltas = if (estadoActual.modoEditar) {
                (estadoActual.faltasEquipo2 - 1).coerceAtLeast(0)
            } else {
                estadoActual.faltasEquipo2 + 1
            }
            estadoActual.copy(faltasEquipo2 = nuevasFaltas)
        }
    }


    fun limpiarError() {
        _state.update { estadoActual ->
            estadoActual.copy(error = null)
        }
    }

}