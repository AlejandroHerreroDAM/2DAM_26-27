package com.example.practicacheckboxescompose.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.practicacheckboxescompose.di.AppModule
import com.example.practicacheckboxescompose.domain.model.Tarea
import com.example.practicacheckboxescompose.ui.componentes.BarraControlesMasivos
import com.example.practicacheckboxescompose.ui.componentes.FormularioTareaCard
import com.example.practicacheckboxescompose.ui.componentes.ItemTareaCard
import com.example.practicacheckboxescompose.ui.componentes.ListaElementosEdicion
import com.example.practicacheckboxescompose.ui.componentes.TopBarTareas
import com.example.practicacheckboxescompose.ui.theme.PracticaCheckboxesComposeTheme

// Activity Android: punto de entrada que monta el contenido Compose.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaCheckboxesComposeTheme {
                val vm: MainViewModel = viewModel(
                    factory = MainViewModelFactory(
                        AppModule.cargarTareasUseCase,
                        AppModule.addTareaUseCase,
                        AppModule.delTareasUseCase,
                        AppModule.updateTareaUseCase
                    )
                )
                MainScreen(vm)
            }
        }
    }
}

// Componente conectado al ViewModel: convierte StateFlow en estado observable.
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()
    MainScreenContent(
        state = state,
        onToggleCheck = viewModel::handleToggleCheck,
        onMarcarTodas = viewModel::handleMarcarODesmarcarTodas,
        onCompletar = viewModel::handleCompletarMarcadas,
        onBorrar = viewModel::handleBorrarMarcadas,
        onBusquedaChange = viewModel::handleBusquedaChange,
        onFiltroChange = viewModel::handleFiltroChange,
        onToggleCompletada = viewModel::handleToggleCompletada,
        onAbrirEdicion = viewModel::handleAbrirEdicion,
        onGuardarEdicion = viewModel::handleGuardarEdicion,
        onAdd = viewModel::handleAdd,
        onEliminarTituloEdicion = viewModel::handleEliminarTituloEdicion,
        onEliminarCategoriaEdicion = viewModel::handleEliminarCategoriaEdicion,
        onEliminarEstadoEdicion = viewModel::handleEliminarEstadoEdicion
    )
}

// Componente UI stateless: recibe datos y callbacks, sin conocer el ViewModel.
@Composable
fun MainScreenContent(
    state: MainState,
    onToggleCheck: (Int) -> Unit,
    onMarcarTodas: () -> Unit,
    onCompletar: () -> Unit,
    onBorrar: () -> Unit,
    onBusquedaChange: (String) -> Unit,
    onFiltroChange: (FiltroTarea) -> Unit,
    onToggleCompletada: (Tarea) -> Unit,
    onAbrirEdicion: (Tarea?) -> Unit,
    onGuardarEdicion: (Tarea) -> Unit,
    onAdd: (String, String) -> Unit,
    onEliminarTituloEdicion: () -> Unit,
    onEliminarCategoriaEdicion: () -> Unit,
    onEliminarEstadoEdicion: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it) }
    }
    // Scaffold organiza barra superior, contenido y mensajes tipo Snackbar.
    Scaffold(
        topBar = { TopBarTareas(state.idsMarcados.size) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            BarraControlesMasivos(
                state.textoBusqueda, state.filtroEstado, state.idsMarcados.size,
                state.tareasFiltradas.isNotEmpty() &&
                    state.tareasFiltradas.all { it.id in state.idsMarcados },
                onBusquedaChange, onFiltroChange, onMarcarTodas, onCompletar, onBorrar
            )
            val tareaEnEdicion = state.tareaEnEdicion
            if (tareaEnEdicion != null) {
                // Sustituye el listado principal por una lista con los elementos editables.
                ListaElementosEdicion(
                    tarea = tareaEnEdicion,
                    onEliminarTitulo = onEliminarTituloEdicion,
                    onEliminarCategoria = onEliminarCategoriaEdicion,
                    onEliminarEstado = onEliminarEstadoEdicion,
                    onCerrar = { onAbrirEdicion(null) }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(state.tareasFiltradas, key = { it.id }) { tarea ->
                        ItemTareaCard(
                            tarea, tarea.id in state.idsMarcados,
                            { onToggleCheck(tarea.id) },
                            { onToggleCompletada(tarea) },
                            { onAbrirEdicion(tarea) }
                        )
                    }
                }
                FormularioTareaCard(onAdd = { titulo, categoria -> onAdd(titulo, categoria) })
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    val tareas = listOf(
        Tarea(1, "Tarea marcada", "Trabajo"),
        Tarea(2, "Otra marcada", "Estudios"),
        Tarea(3, "Tarea pendiente", "Personal")
    )
    PracticaCheckboxesComposeTheme {
        MainScreenContent(
            MainState(tareas = tareas, idsMarcados = setOf(1, 2)),
            {}, {}, {}, {}, {}, {}, {}, {}, {}, { _, _ -> }, {}, {}, {}
        )
    }
}
