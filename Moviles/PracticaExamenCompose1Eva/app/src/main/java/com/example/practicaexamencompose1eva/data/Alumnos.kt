package com.example.practicaexamencompose1eva.data

import com.example.practicaexamencompose1eva.domain.model.Alumno

object Alumnos {
    private var nextId = 1

    private val listaAlumnos = mutableListOf(
        Alumno(id = nextId++, nombre = "Lucía", apellidos = "Álvarez García", curso = "1º DAM", edad = 18, email = "lucia.alvarez@iesquevedo.es", activo = true),
        Alumno(id = nextId++, nombre = "Marco", apellidos = "Castillo Ruiz", curso = "2º ASIR", edad = 19, email = "marco.castillo@iesquevedo.es", activo = true),
        Alumno(id = nextId++, nombre = "Sofía", apellidos = "Pérez Martín", curso = "1º SMR", edad = 17, email = "sofia.perez@iesquevedo.es", activo = false),
        Alumno(id = nextId++, nombre = "Daniel", apellidos = "Rodríguez López", curso = "2º DAM", edad = 20, email = "daniel.rodriguez@iesquevedo.es", activo = true),
        Alumno(id = nextId++, nombre = "Elena", apellidos = "Moreno Sánchez", curso = "1º ASIR", edad = 18, email = "elena.moreno@iesquevedo.es", activo = true)
    )

    fun getAlumnos(): List<Alumno> = listaAlumnos.toList()

    fun addAlumno(alumno: Alumno) {
        listaAlumnos.add(alumno.copy(id = nextId++))
    }

    fun deleteAlumno(alumno: Alumno) {
        listaAlumnos.removeAll { it.id == alumno.id }
    }

    fun updateAlumno(alumnoActualizado: Alumno) {
        val index = listaAlumnos.indexOfFirst { it.id == alumnoActualizado.id }
        if (index != -1) {
            listaAlumnos[index] = alumnoActualizado
        }
    }
}