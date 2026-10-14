// Declara el paquete de la capa de datos.
package com.example.mvvm_ejercicio_alejandro.data

// Importa el modelo que representa un coche.
import com.example.mvvm_ejercicio_alejandro.domain.model.Coche

// Define un objeto singleton que actúa como base de datos en memoria.
object DBCoches {
    // Crea una lista mutable con los coches iniciales de la aplicación.
    private val listaCoches = mutableListOf(
        // Añade el primer coche a la lista.
        Coche("Seat", "Ibiza", 1),
        // Añade el segundo coche a la lista.
        Coche("Honda","Civic",2),
        // Añade el tercer coche a la lista.
        Coche("Volskwagen","Golf",3)
    )

    // Devuelve una copia de solo lectura de todos los coches almacenados.
    fun getAllCoches() = listaCoches.toList()

    // Recibe un coche y lo añade a la lista en memoria.
    fun addCoche(c: Coche){
        // Inserta el coche recibido al final de la lista.
        listaCoches.add(c)
    }

    // Recibe un coche y trata de eliminarlo de la lista.
    fun removeCoche(c: Coche){
        // Elimina el coche usando la igualdad definida por data class.
        listaCoches.remove(c)
    }

    // Devuelve aleatoriamente uno de los coches almacenados.
    fun getRandomCoche() = listaCoches.random()
}