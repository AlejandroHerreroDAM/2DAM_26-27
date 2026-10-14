// Declara el paquete de la pantalla principal.
package com.example.mvvm_ejercicio_alejandro.ui.main

// Permite exponer datos observables sin permitir que la vista los modifique.
import androidx.lifecycle.LiveData
// Implementación mutable de LiveData usada internamente por el ViewModel.
import androidx.lifecycle.MutableLiveData
// Clase base que conserva el estado durante cambios de configuración.
import androidx.lifecycle.ViewModel
// Clases necesarias para crear el ViewModel con dependencias.
import androidx.lifecycle.ViewModelProvider
// Importa el modelo de coche.
import com.example.mvvm_ejercicio_alejandro.domain.model.Coche
// Importa el caso de uso de alta.
import com.example.mvvm_ejercicio_alejandro.domain.useCases.AddCocheUseCase
// Importa el caso de uso de consulta.
import com.example.mvvm_ejercicio_alejandro.domain.useCases.GetAllCochesUseCase
// Importa el caso de uso aleatorio.
import com.example.mvvm_ejercicio_alejandro.domain.useCases.GetRandomCocheUseCase
// Importa el caso de uso de borrado.
import com.example.mvvm_ejercicio_alejandro.domain.useCases.RemoveCocheUseCase

// ViewModel que coordina la lógica de la pantalla principal.
class MainViewModel(
    // Caso de uso para añadir coches.
    private val addCocheUseCase: AddCocheUseCase,
    // Caso de uso para obtener todos los coches.
    private val getAllCochesUseCase: GetAllCochesUseCase,
    // Caso de uso para eliminar coches.
    private val removeCocheUseCase: RemoveCocheUseCase,
    // Caso de uso para obtener un coche aleatorio.
    private val getRandomCocheUseCase: GetRandomCocheUseCase,
) : ViewModel() {

    // Estado mutable privado que solo puede actualizar este ViewModel.
    private val _state = MutableLiveData<MainState>(MainState())
    // Estado público de solo lectura que observa la actividad.
    val state: LiveData<MainState> = _state

    // Bloque ejecutado al crear el ViewModel.
    init {
        // Carga inicialmente todos los coches en el estado.
        handleGetAllCoches()
    }

    // Selecciona un coche aleatorio y muestra su marca.
    fun handleBotonPulsado(){
        // Solicita un coche aleatorio al caso de uso correspondiente.
        val cocheAleatorio = getRandomCocheUseCase.getRandomCoche()
        // Copia el estado actual sustituyendo el texto de la pantalla.
        _state.value = _state.value?.copy(textoPantalla = cocheAleatorio.marca)
    }

    // Actualiza el estado con todos los coches existentes.
    fun handleGetAllCoches(){
        // Obtiene la lista y la guarda en una nueva copia del estado.
        _state.value = _state.value?.copy(listaCoches = getAllCochesUseCase.getAllCoches() )
    }

    // Añade un coche y refresca la lista mostrada.
    fun handleAddCoche(c: Coche){
       // Captura posibles errores producidos durante la operación.
       try {
           // Delega la inserción en el caso de uso.
           addCocheUseCase.addCoche(c)
           // Vuelve a consultar la lista para reflejar el cambio.
           _state.value = _state.value?.copy(listaCoches = getAllCochesUseCase.getAllCoches())
       } catch (e: Exception){
           // Guarda el mensaje del error para que la interfaz pueda mostrarlo.
           _state.value = _state.value?.copy(error = e.message)
       }
    }

    // Elimina un coche y refresca la lista mostrada.
    fun handleRemoveCoche(c: Coche){
        // Captura posibles errores producidos durante la operación.
        try {
            // Delega la eliminación en el caso de uso.
            removeCocheUseCase.removeCoche(c)
            // Vuelve a consultar la lista para reflejar el cambio.
            _state.value = _state.value?.copy(listaCoches = getAllCochesUseCase.getAllCoches())
        } catch (e: Exception){
            // Guarda el mensaje del error para que la interfaz pueda mostrarlo.
            _state.value = _state.value?.copy(error = e.message)
        }
    }

    // Borra el mensaje de error del estado.
    fun handleLimpiarError() {
        // Copia el estado manteniendo el resto de datos y anulando el error.
        _state.value = _state.value?.copy(error = null)
    }

}

// Fábrica que construye el MainViewModel con sus casos de uso.
class MainViewModelFactory(
    // Caso de uso para consultar todos los coches.
    val getAllCochesUseCase: GetAllCochesUseCase,
    // Caso de uso para añadir coches.
    val addCocheUseCase: AddCocheUseCase,
    // Caso de uso para eliminar coches.
    val removeCocheUseCase: RemoveCocheUseCase,
    // Caso de uso para obtener un coche aleatorio.
    val getRandomCocheUseCase: GetRandomCocheUseCase,
) : ViewModelProvider.Factory{
    // Crea una instancia del ViewModel solicitado.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprueba que la clase solicitada corresponde al ViewModel esperado.
        if (modelClass.isAssignableFrom(com.example.mvvm_ejercicio_alejandro.ui.main.MainViewModel::class.java)) {
            // Indica al compilador que el tipo es seguro tras la comprobación anterior.
            @Suppress("UNCHECKED_CAST") return MainViewModel(
                // Pasa el caso de uso de alta al ViewModel.
                addCocheUseCase,
                // Pasa el caso de uso de consulta al ViewModel.
                getAllCochesUseCase,
                // Pasa el caso de uso de borrado al ViewModel.
                removeCocheUseCase,
                // Pasa el caso de uso aleatorio al ViewModel.
                getRandomCocheUseCase,
            ) as T
        }
        // Informa de que se solicitó un ViewModel no soportado.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}