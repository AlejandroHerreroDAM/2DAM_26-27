package com.example.mvvm_ejercicio_alejandro.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_ejercicio_alejandro.domain.model.Coche
import com.example.mvvm_ejercicio_alejandro.domain.useCases.AddCocheUseCase
import com.example.mvvm_ejercicio_alejandro.domain.useCases.GetAllCochesUseCase
import com.example.mvvm_ejercicio_alejandro.domain.useCases.GetRandomCocheUseCase
import com.example.mvvm_ejercicio_alejandro.domain.useCases.RemoveCocheUseCase

class MainViewModel(
    private val addCocheUseCase: AddCocheUseCase,
    private val getAllCochesUseCase: GetAllCochesUseCase,
    private val removeCocheUseCase: RemoveCocheUseCase,
    private val getRandomCocheUseCase: GetRandomCocheUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<MainState>(MainState())
    val state: LiveData<MainState> = _state

    init {
        handleGetAllCoches()

    }

    fun handleBotonPulsado(){
        val cocheAleatorio = getRandomCocheUseCase.getRandomCoche()
        _state.value = _state.value?.copy(textoPantalla = cocheAleatorio.marca)
    }


    fun handleGetAllCoches(){

        _state.value = _state.value?.copy(listaCoches = getAllCochesUseCase.getAllCoches() )
    }

    fun handleAddCoche(c: Coche){

       try {
           addCocheUseCase.addCoche(c)
           _state.value = _state.value?.copy(listaCoches = getAllCochesUseCase.getAllCoches())
       } catch (e: Exception){
           _state.value = _state.value?.copy(error = e.message)
       }
    }

    fun handleRemoveCoche(c: Coche){
        try {
            removeCocheUseCase.removeCoche(c)
            _state.value = _state.value?.copy(listaCoches = getAllCochesUseCase.getAllCoches())
        } catch (e: Exception){
            _state.value = _state.value?.copy(error = e.message)
        }
    }

    fun handleLimpiarError() {
        _state.value = _state.value?.copy(error = null)
    }

}

class MainViewModelFactory(
    val getAllCochesUseCase: GetAllCochesUseCase,
    val addCocheUseCase: AddCocheUseCase,
    val removeCocheUseCase: RemoveCocheUseCase,
    val getRandomCocheUseCase: GetRandomCocheUseCase,
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.example.mvvm_ejercicio_alejandro.ui.main.MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MainViewModel(
                addCocheUseCase,
                getAllCochesUseCase,
                removeCocheUseCase,
                getRandomCocheUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}