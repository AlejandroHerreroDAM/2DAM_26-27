package com.example.mvvmprueba_alejandro.domain.useCases

import com.example.mvvmprueba_alejandro.data.Coches
import com.example.mvvmprueba_alejandro.domain.model.Coche

class addCocheUseCase {

    fun addCoche(c: Coche) = Coches.add(c)

}