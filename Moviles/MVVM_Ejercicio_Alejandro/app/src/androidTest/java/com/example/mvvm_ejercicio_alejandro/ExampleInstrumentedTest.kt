// Declara el paquete de los tests instrumentados.
package com.example.mvvm_ejercicio_alejandro

// Permite acceder al contexto de la aplicación bajo prueba.
import androidx.test.platform.app.InstrumentationRegistry
// Runner que ejecuta los tests con AndroidJUnit.
import androidx.test.ext.junit.runners.AndroidJUnit4

// Importa la anotación que marca un método como test.
import org.junit.Test
// Importa la anotación que selecciona el runner del test.
import org.junit.runner.RunWith

// Importa las aserciones de JUnit.
import org.junit.Assert.*

/**
 * Test instrumentado que se ejecuta en un dispositivo o emulador Android.
 *
 * Consulta la documentación de testing en el enlace original.
 */
// Indica que la clase debe ejecutarse con AndroidJUnit4.
@RunWith(AndroidJUnit4::class)
// Agrupa los tests instrumentados de ejemplo.
class ExampleInstrumentedTest {
    // Indica que este método debe ejecutarse como test.
    @Test
    // Comprueba que se puede obtener el contexto de la aplicación.
    fun useAppContext() {
        // Obtiene el contexto de la aplicación instalada bajo prueba.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // Verifica que el paquete de la aplicación es el esperado.
        assertEquals("com.example.mvvm_ejercicio_alejandro", appContext.packageName)
    }
}