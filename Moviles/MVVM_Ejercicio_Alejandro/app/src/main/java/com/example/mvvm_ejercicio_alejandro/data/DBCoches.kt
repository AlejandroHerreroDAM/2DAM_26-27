package com.example.mvvm_ejercicio_alejandro.data
import com.example.mvvm_ejercicio_alejandro.domain.model.Coche
object DBCoches {
    private val listaCoches = mutableListOf(
        Coche("Seat", "Ibiza", 1),
        Coche("Honda","Civic",2),
        Coche("Volskwagen","Golf",3)
    )
    fun getAllCoches() = listaCoches.toList()
    fun addCoche(c: Coche){
        listaCoches.add(c)
    }
    fun removeCoche(c: Coche){
        listaCoches.remove(c)
    }
    fun getRandomCoche() = listaCoches.random()
}