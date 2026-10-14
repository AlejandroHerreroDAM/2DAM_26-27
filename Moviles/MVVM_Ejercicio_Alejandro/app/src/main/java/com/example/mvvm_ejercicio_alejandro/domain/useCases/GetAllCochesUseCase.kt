// Declara el paquete de casos de uso.
package com.example.mvvm_ejercicio_alejandro.domain.useCases

// Importa la fuente de datos de coches.
import com.example.mvvm_ejercicio_alejandro.data.DBCoches

// Encapsula la operación de consultar todos los coches.
class GetAllCochesUseCase {

    // Devuelve la lista de coches proporcionada por la base de datos.
    fun getAllCoches() = DBCoches.getAllCoches()
}