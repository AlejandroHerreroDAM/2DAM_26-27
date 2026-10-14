// Declara el paquete de casos de uso.
package com.example.mvvm_ejercicio_alejandro.domain.useCases

// Importa la fuente de datos de coches.
import com.example.mvvm_ejercicio_alejandro.data.DBCoches
// Importa el modelo de coche.
import com.example.mvvm_ejercicio_alejandro.domain.model.Coche

// Encapsula la operación de eliminar un coche.
class RemoveCocheUseCase {

    // Delega en la base de datos en memoria la eliminación del coche.
    fun removeCoche(c: Coche) = DBCoches.removeCoche(c)
}