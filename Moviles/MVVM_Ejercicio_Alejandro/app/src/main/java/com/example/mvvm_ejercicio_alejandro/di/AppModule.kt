// Declara el paquete encargado de proporcionar dependencias.
package com.example.mvvm_ejercicio_alejandro.di

// Importa el caso de uso para añadir coches.
import com.example.mvvm_ejercicio_alejandro.domain.useCases.AddCocheUseCase
// Importa el caso de uso para consultar todos los coches.
import com.example.mvvm_ejercicio_alejandro.domain.useCases.GetAllCochesUseCase
// Importa el caso de uso para obtener un coche aleatorio.
import com.example.mvvm_ejercicio_alejandro.domain.useCases.GetRandomCocheUseCase
// Importa el caso de uso para eliminar coches.
import com.example.mvvm_ejercicio_alejandro.domain.useCases.RemoveCocheUseCase

// Singleton que centraliza la creación de las dependencias de la aplicación.
object AppModule {

    // Crea una única instancia del caso de uso de alta.
    val addCocheUseCase = AddCocheUseCase()
    // Crea una única instancia del caso de uso de consulta completa.
    val getAllCochesUseCase = GetAllCochesUseCase()
    // Crea una única instancia del caso de uso de borrado.
    val removeCocheUseCase = RemoveCocheUseCase()
    // Crea una única instancia del caso de uso aleatorio.
    val getRandomCocheUseCase = GetRandomCocheUseCase()
}