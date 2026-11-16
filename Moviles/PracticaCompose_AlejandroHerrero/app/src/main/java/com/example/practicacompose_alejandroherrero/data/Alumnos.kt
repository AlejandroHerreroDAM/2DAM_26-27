package com.example.practicacompose_alejandroherrero.data

import com.example.practicacompose_alejandroherrero.domain.model.Alumno

object Alumnos {

    private var nextId = 1L
    private var alumnos= mutableListOf(
        Alumno(
            id = nextId ++,
            nombre="Lucía",
            apellido="Álvarez",
            curso = "1ºDAM",
            edad = 18,
            correo= "lucia@gmail.com",
            activo = true,
        ),
        Alumno(
            id = nextId ++,
            nombre="Marco",
            apellido="Castillo",
            curso = "2ºASIR",
            edad = 19,
            correo= "marco@gmail.com",
            activo = true,
        ),
        Alumno(
            id = nextId ++,
            nombre="Sofia",
            apellido="Perez",
            curso = "2ºDAM",
            edad = 20,
            correo= "sofia@gmail.com",
            activo = false,
        )
    )

    fun getListaAlumnos() = alumnos.toList()

    fun addAlumno(a: Alumno){
        alumnos.add(a.copy(id = nextId++))

    }

    fun deleteAlumno(a: Alumno){
        alumnos.removeAll { it.id == a.id }
    }
}