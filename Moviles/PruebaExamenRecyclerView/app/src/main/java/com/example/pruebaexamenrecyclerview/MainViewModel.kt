package com.example.pruebaexamenrecyclerview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// CEREBRO: Guarda los datos (MainState) y decide qué pasa cuando el usuario interactúa.
// Nunca toca un elemento del XML directamente (no sabe que existe MainActivity).
class MainViewModel : ViewModel() {

    // Variable privada que podemos modificar (.value) y variable pública de solo lectura para observar.
    private val _state = MutableLiveData<MainState>(MainState(listOf(), "Cargando Inventario..."))
    val state: LiveData<MainState> = _state

    // Se ejecuta automáticamente al arrancar la app. Carga los datos iniciales.
    init {
        val inventarioInicial = listOf(
            Articulo("Manzanas", 2.5, 10),
            Articulo("Pan", 1.0, 5),
            Articulo("Leche", 1.2, 8)
        )
        _state.value = _state.value?.copy(articulos = inventarioInicial)
    }

    // ACCIÓN: Cambia SOLO el texto de cabecera en el estado.
    fun handleArticuloSeleccionado(articulo: Articulo) {
        _state.value = _state.value?.copy(articuloSeleccionado = "Seleccionado: ${articulo.nombre}")
    }

    // ACCIÓN: Coge la lista, le quita el último elemento, y actualiza el estado.
    fun handleEliminarUltimo() {
        val listaActual = _state.value?.articulos ?: emptyList()

        if (listaActual.isNotEmpty()) {
            val nuevaLista = listaActual.dropLast(1)
            _state.value = _state.value?.copy(articulos = nuevaLista)
        }
    }

    // ACCIÓN: Coge la lista, le suma un artículo nuevo, y actualiza el estado.
    fun handleAnadirArticulo() {
        val listaActual = _state.value?.articulos ?: emptyList()
        val articuloNuevo = Articulo("Nuevo Producto", 5.0, 1)

        val nuevaLista = listaActual + articuloNuevo
        _state.value = _state.value?.copy(articulos = nuevaLista)
    }


    /**
     * PARA ELIMINAR UN ARTICULO AL DARLE CLICK
     *
    fun handleBorrarArticulo(articuloTocado: Articulo) {
    val listaActual = _state.value?.articulos ?: emptyList()

    val nuevaLista = listaActual.filter { it != articuloTocado }
    _state.value = _state.value?.copy(articulos = nuevaLista)
    }
     **/

}