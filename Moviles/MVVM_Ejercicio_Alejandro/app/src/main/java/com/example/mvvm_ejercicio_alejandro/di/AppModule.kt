package com.example.mvvm_ejercicio_alejandro.di
import com.example.mvvm_ejercicio_alejandro.domain.useCases.AddCocheUseCase
import com.example.mvvm_ejercicio_alejandro.domain.useCases.GetAllCochesUseCase
import com.example.mvvm_ejercicio_alejandro.domain.useCases.GetRandomCocheUseCase
import com.example.mvvm_ejercicio_alejandro.domain.useCases.RemoveCocheUseCase
object AppModule {
    val addCocheUseCase = AddCocheUseCase()
    val getAllCochesUseCase = GetAllCochesUseCase()
    val removeCocheUseCase = RemoveCocheUseCase()
    val getRandomCocheUseCase = GetRandomCocheUseCase()
}