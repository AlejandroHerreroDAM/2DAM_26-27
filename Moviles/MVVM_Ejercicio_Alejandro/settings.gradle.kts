// Configura dónde Gradle busca plugins y dependencias.
pluginManagement {
    // Declara los repositorios de plugins.
    repositories {
        // Repositorio de Google, limitado a grupos Android y Google.
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
// Declara plugins usados por Gradle para resolver versiones de Java.
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
// Configura los repositorios de dependencias para todos los proyectos.
dependencyResolutionManagement {
    // Impide declarar repositorios diferentes dentro de los módulos.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    // Repositorios donde se buscan las bibliotecas.
    repositories {
        google()
        mavenCentral()
    }
}

// Define el nombre del proyecto raíz.
rootProject.name = "MVVM_Ejercicio_Alejandro"
// Incluye el módulo de aplicación.
include(":app")
 