// Aplica la configuración del plugin de aplicación Android mediante el catálogo de versiones.
plugins {
    alias(libs.plugins.android.application)
}

// Configura las opciones específicas del módulo Android.
android {
    // Define el namespace de las clases generadas y del código de la aplicación.
    namespace = "com.example.mvvm_ejercicio_alejandro"
    // Indica la versión del SDK usada para compilar.
    compileSdk {
        version = release(37)
    }

    // Define los valores por defecto de todas las variantes.
    defaultConfig {
        // Identificador único de la aplicación instalada.
        applicationId = "com.example.mvvm_ejercicio_alejandro"
        // Versión mínima de Android compatible.
        minSdk = 24
        // Versión de Android cuyo comportamiento se toma como objetivo.
        targetSdk = 37
        // Número interno de versión para actualizaciones.
        versionCode = 1
        // Nombre visible de la versión.
        versionName = "1.0"

        // Clase que ejecuta los tests instrumentados.
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Configura las variantes de compilación.
    buildTypes {
        // Configuración usada para generar la versión de producción.
        release {
            // Desactiva la optimización específica configurada para release.
            optimization {
                enable = false
            }
        }
    }
    // Configura la compatibilidad del código Java/Kotlin con Java 11.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Activa características adicionales del plugin Android.
    buildFeatures {
        // Genera clases de enlace para acceder a las vistas sin findViewById.
        viewBinding = true
    }
}

// Declara las bibliotecas usadas por la aplicación y sus tests.
dependencies {
    // Añade extensiones Kotlin para Activity.
    implementation(libs.androidx.activity.ktx)
    // Añade compatibilidad con actividades AppCompat.
    implementation(libs.androidx.appcompat)
    // Añade el sistema de layouts ConstraintLayout.
    implementation(libs.androidx.constraintlayout)
    // Añade utilidades básicas de AndroidX.
    implementation(libs.androidx.core.ktx)
    // Añade componentes visuales de Material.
    implementation(libs.material)
    // Añade JUnit para tests locales.
    testImplementation(libs.junit)
    // Añade Espresso para tests de interfaz en dispositivo.
    androidTestImplementation(libs.androidx.espresso.core)
    // Añade utilidades AndroidX para tests instrumentados.
    androidTestImplementation(libs.androidx.junit)
}