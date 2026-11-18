package com.example.practicaexamenxml1eva.domain.model

data class Alumno(
    // Campos del alumno asociados al formulario y a las filas.
    val id: Int = 0,
    val nombre: String,
    val apellidos: String,
    val curso: String,
    val edad: Int,
    val email: String,
    val activo: Boolean
) {
    fun obtenerIniciales(): String {
        val inicialNombre = nombre.trim().firstOrNull()?.uppercaseChar() ?: ""
        val inicialApellido = apellidos.trim().firstOrNull()?.uppercaseChar() ?: ""
        return "$inicialNombre$inicialApellido"
    }

    fun obtenerAtributos(): List<Pair<String, String>> {
        val lista = mutableListOf<Pair<String, String>>()
        if (nombre.isNotBlank()) lista.add("Nombre" to nombre)
        if (apellidos.isNotBlank()) lista.add("Apellidos" to apellidos)
        if (curso.isNotBlank()) lista.add("Curso" to curso)
        if (edad > 0) lista.add("Edad" to "$edad años")
        if (email.isNotBlank()) lista.add("Email" to email)
        if (activo) lista.add("Estado" to "Activo")
        return lista
    }
}