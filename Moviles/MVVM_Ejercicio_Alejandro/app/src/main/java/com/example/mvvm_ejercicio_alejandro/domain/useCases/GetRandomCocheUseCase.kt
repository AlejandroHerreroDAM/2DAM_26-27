// Declara el paquete de casos de uso.
package com.example.mvvm_ejercicio_alejandro.domain.useCases

// Importa la fuente de datos de coches.
import com.example.mvvm_ejercicio_alejandro.data.DBCoches

// Encapsula la operación de seleccionar un coche aleatorio.
class GetRandomCocheUseCase {

    // Solicita a la base de datos un coche elegido al azar.
    fun getRandomCoche() = DBCoches.getRandomCoche()
}