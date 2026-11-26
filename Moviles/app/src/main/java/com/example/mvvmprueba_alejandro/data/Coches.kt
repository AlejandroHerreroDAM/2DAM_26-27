package com.example.mvvmprueba_alejandro.data

import com.example.mvvmprueba_alejandro.domain.model.Coche
import kotlin.random.Random

object Coches {

    private val coches = mutableListOf(
        Coche("Seat","Ibiza",123456),
        Coche("Honda","Civic",987654),
    )

    fun dameCoche() = coches[1]

    fun dameLista() = coches.toList()

    fun add(c: Coche){
        if (Random.nextDouble()>0.60)
            throw RuntimeException("Error al añadir")
        coches.add(c)
    }

    fun del(c: Coche){
        if (Random.nextDouble()>0.40)
            throw RuntimeException("Error al añadir")
        coches.remove(c)
    }


}