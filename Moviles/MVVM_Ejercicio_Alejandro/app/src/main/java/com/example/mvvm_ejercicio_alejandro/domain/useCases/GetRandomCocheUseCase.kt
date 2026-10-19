package com.example.mvvm_ejercicio_alejandro.domain.useCases
import com.example.mvvm_ejercicio_alejandro.data.DBCoches
class GetRandomCocheUseCase {
    fun getRandomCoche() = DBCoches.getRandomCoche()
}