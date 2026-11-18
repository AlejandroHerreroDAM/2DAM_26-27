package com.example.practicacheckboxescompose

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Prueba instrumentada: se ejecuta en un dispositivo o emulador Android.
 * Permite leer el contexto real de la aplicacion y comprobar configuraciones del APK.
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.practicacheckboxescompose", appContext.packageName)
    }
}
