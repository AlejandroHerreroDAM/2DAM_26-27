package com.example.practicacheckboxescompose.ui.componentes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarTareas(idsMarcados: Int) {
    // Barra superior fija que muestra el titulo y la cantidad seleccionada.
    TopAppBar(
        title = { Text("Gestor de Tareas") },
        navigationIcon = {},
        actions = { Text("Marcadas: $idsMarcados") },
        colors = TopAppBarDefaults.topAppBarColors()
    )
}
