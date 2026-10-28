package com.example.pruebaexamenrecyclerview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){

    private val _state = MutableLiveData<MainState>(MainState(listOf(), "Cargando Inventario..."))
    val state: LiveData<MainState> = _state

    init {
        val inventarioInicial = listOf(
            Articulo("Manzanas", 2.5, 10),
            Articulo("Pan", 1.0, 5),
            Articulo("Leche", 1.2, 8)
        )
        _state.value = _state.value?.copy(articulos = inventarioInicial)
    }


    fun handleArticuloSeleccionado(articulo: Articulo){
        _state.value = _state.value?.copy(articuloSeleccionado = "Seleccionado: ${articulo.nombre}")
    }

    fun handleEliminarUltimo() {
        val listaActual = _state.value?.articulos ?: emptyList()

        if (listaActual.isNotEmpty()) {
            val nuevaLista = listaActual.dropLast(1)

            _state.value = _state.value?.copy(articulos = nuevaLista)
        }
    }

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