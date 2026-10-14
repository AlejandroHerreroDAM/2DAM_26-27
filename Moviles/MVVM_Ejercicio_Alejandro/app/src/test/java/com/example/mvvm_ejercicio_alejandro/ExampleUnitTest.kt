// Declara el paquete de los tests locales.
package com.example.mvvm_ejercicio_alejandro

// Importa la anotación que marca un método como test.
import org.junit.Test

// Importa las aserciones de JUnit.
import org.junit.Assert.*

/**
 * Test local de ejemplo que se ejecuta en la máquina de desarrollo.
 *
 * Consulta la documentación de testing en el enlace original.
 */
// Agrupa los tests unitarios de ejemplo.
class ExampleUnitTest {
    // Indica que este método debe ejecutarse como test.
    @Test
    // Comprueba que la suma básica produce el resultado esperado.
    fun addition_isCorrect() {
        // Verifica que 2 + 2 es igual a 4.
        assertEquals(4, 2 + 2)
    }
}