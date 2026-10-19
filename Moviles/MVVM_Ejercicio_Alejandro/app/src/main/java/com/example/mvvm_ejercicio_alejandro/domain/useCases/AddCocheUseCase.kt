package com.example.mvvm_ejercicio_alejandro.domain.useCases
import com.example.mvvm_ejercicio_alejandro.data.DBCoches
import com.example.mvvm_ejercicio_alejandro.domain.model.Coche
class AddCocheUseCase {
    fun addCoche(c: Coche) = DBCoches.addCoche(c)
}