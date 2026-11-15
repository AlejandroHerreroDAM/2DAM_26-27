package com.example.plantillaexamencompose

import androidx.lifecycle.ViewModel
import com.example.plantillaexamencompose.model.Elemento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * VIEWMODEL (Cerebro de la pantalla)
 * Hereda de ViewModel() para sobrevivir a los giros de pantalla sin perder los datos.
 * Aquí va TODA la lógica (validaciones, cálculos, añadir, borrar). La vista nunca toma decisiones.
 */
class MainViewModel : ViewModel() {

    // 1. Estado privado y editable: solo el ViewModel puede modificarlo
    private val _state = MutableStateFlow(MainState())

    // 2. Estado público de solo lectura: la vista lo observa mediante collectAsState()
    val state: StateFlow<MainState> = _state.asStateFlow()

    // Contador interno para asignar un ID único a cada elemento nuevo
    private var siguienteId = 1

    /**
     * Se ejecuta cada vez que el usuario teclea una letra en el campo de texto.
     * Actualiza 'textoInput' en el estado para que el TextField muestre lo que se escribe.
     */
    fun onTextoChange(nuevoTexto: String) {
        _state.update { estadoActual ->
            estadoActual.copy(textoInput = nuevoTexto)
        }
    }

    /**
     * Valida el texto, crea un nuevo Elemento y lo añade a la lista.
     */
    fun onAgregarElemento() {
        val textoLimpio = _state.value.textoInput.trim()

        // Validación: si está vacío, avisamos mediante 'error' (dispara el Snackbar) y salimos
        if (textoLimpio.isEmpty()) {
            _state.update { it.copy(error = "Escribe una palabra antes de añadir") }
            return
        }

        val nuevoElemento = Elemento(
            id = siguienteId++,
            nombre = textoLimpio
        )

        // En Compose no se usa lista.add(); se crea una lista nueva con '+' para que detecte el cambio
        _state.update { estadoActual ->
            estadoActual.copy(
                listaElementos = estadoActual.listaElementos + nuevoElemento,
                textoInput = "" // Limpiamos el campo de texto tras añadir
            )
        }
    }

    /**
     * Marca o desmarca la fila sobre la que el usuario ha hecho clic en la lista.
     */
    fun onSeleccionarElemento(elemento: Elemento) {
        _state.update { estadoActual ->
            // Si pulsas el mismo que ya estaba marcado, se deselecciona (pasa a null)
            val nuevaSeleccion = if (estadoActual.elementoSeleccionado?.id == elemento.id) {
                null
            } else {
                elemento
            }
            estadoActual.copy(elementoSeleccionado = nuevaSeleccion)
        }
    }

    /**
     * Elimina de la lista el elemento que esté marcado en 'elementoSeleccionado'.
     */
    fun onBorrarSeleccionado() {
        val seleccionado = _state.value.elementoSeleccionado

        // Validación: si no hay ninguna fila marcada, avisamos con el Snackbar
        if (seleccionado == null) {
            _state.update { it.copy(error = "Selecciona una fila de la lista para borrarla") }
            return
        }

        // .filter crea una lista nueva excluyendo el elemento cuyo id coincida con el seleccionado
        _state.update { estadoActual ->
            estadoActual.copy(
                listaElementos = estadoActual.listaElementos.filter { it.id != seleccionado.id },
                elementoSeleccionado = null // Quitamos la selección porque ese objeto ya no existe
            )
        }
    }

    /**
     * Resetea el error a null una vez que el Snackbar ya se ha mostrado en pantalla.
     */
    fun limpiarError() {
        _state.update { estadoActual ->
            estadoActual.copy(error = null)
        }
    }
}