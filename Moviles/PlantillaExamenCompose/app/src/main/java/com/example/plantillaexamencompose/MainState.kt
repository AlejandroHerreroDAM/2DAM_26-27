package com.example.plantillaexamencompose

import com.example.plantillaexamencompose.model.Elemento

/**
 * ESTADO DE LA INTERFAZ (UI State)
 * Es una "foto fija" e inmutable (todo con 'val') de todos los datos que necesita la pantalla.
 * La vista (MainActivity) SOLO lee este estado; nunca lo modifica directamente.
 * Cuando algo cambia, el ViewModel crea una copia nueva con .copy(...) y Compose redibuja la pantalla.
 */
data class MainState(
    val titulo: String = "Lista de elementos",       // Título superior de la pantalla
    val textoInput: String = "",                     // Texto actual escrito en el OutlinedTextField
    val listaElementos: List<Elemento> = emptyList(),// Lista de objetos que pintará el LazyColumn
    val elementoSeleccionado: Elemento? = null,      // Fila pulsada actualmente (null si no hay ninguna)
    val loading: Boolean = false,                    // Útil si en el examen piden mostrar un CircularProgressIndicator
    val error: String? = null                        // Si tiene texto, lanza un Snackbar automático y luego vuelve a null
)