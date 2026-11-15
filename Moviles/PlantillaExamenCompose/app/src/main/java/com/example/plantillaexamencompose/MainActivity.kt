package com.example.plantillaexamencompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plantillaexamencompose.model.Elemento
import com.example.plantillaexamencompose.ui.BotonPrincipal
import com.example.plantillaexamencompose.ui.CampoTexto
import com.example.plantillaexamencompose.ui.TarjetaElemento
import com.example.plantillaexamencompose.ui.theme.PlantillaExamenComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Extiende el fondo de la app detrás de la barra de estado y navegación
        setContent {
            PlantillaExamenComposeTheme {
                MainPantalla()
            }
        }
    }
}

/**
 * CAPA CONECTADA AL VIEWMODEL (Stateful)
 * Se encarga exclusivamente de obtener el ViewModel, observar el estado con collectAsState()
 * y pasar los datos y eventos hacia abajo a MainPantallaContent.
 */
@Composable
fun MainPantalla(
    viewModel: MainViewModel = viewModel()
) {
    // Suscribe la vista al StateFlow: cada vez que el ViewModel hace _state.update, esta línea recompone la UI
    val state by viewModel.state.collectAsState()

    MainPantallaContent(
        state = state,
        onTextoChange = { viewModel.onTextoChange(it) },
        onAgregarClick = { viewModel.onAgregarElemento() },
        onSeleccionarElemento = { viewModel.onSeleccionarElemento(it) },
        onBorrarClick = { viewModel.onBorrarSeleccionado() },
        onLimpiarError = { viewModel.limpiarError() }
    )
}

/**
 * CAPA VISUAL PURA (Stateless)
 * No conoce el ViewModel; solo recibe 'state' y funciones lambda por parámetro.
 * Gracias a esto y a los valores por defecto (= {}), podemos dibujarla en @Preview sin errores.
 */
@Composable
fun MainPantallaContent(
    state: MainState,
    onTextoChange: (String) -> Unit = {},
    onAgregarClick: () -> Unit = {},
    onSeleccionarElemento: (Elemento) -> Unit = {},
    onBorrarClick: () -> Unit = {},
    onLimpiarError: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Controlador necesario para mostrar mensajes emergentes (Snackbar) en Material 3
    val snackbarHostState = remember { SnackbarHostState() }

    // Efecto secundario: cada vez que 'state.error' cambia y no es null, lanza el Snackbar y limpia el error
    LaunchedEffect(state.error) {
        state.error?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            onLimpiarError()
        }
    }

    // Scaffold proporciona la estructura base de Material 3 y calcula los márgenes de las barras del sistema
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Evita que el contenido se solape con la barra de hora/batería
                .padding(16.dp)
        ) {
            // 1. Cabecera
            Text(
                text = state.titulo,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // 2. Campo de entrada de texto
            CampoTexto(
                valor = state.textoInput,
                onValorChange = onTextoChange,
                etiqueta = "Escribe un elemento"
            )

            // 3. Fila con los dos botones repartidos al 50% (weight = 1f en cada uno)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                BotonPrincipal(
                    texto = "AÑADIR",
                    onClick = onAgregarClick,
                    modifier = Modifier.weight(1f)
                )

                BotonPrincipal(
                    texto = "BORRAR",
                    onClick = onBorrarClick,
                    colorFondo = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
            }

            // 4. LISTA DINÁMICA (Equivalente al RecyclerView en Jetpack Compose)
            // weight(1f) hace que ocupe todo el espacio vertical sobrante debajo de los botones
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // 'items' recorre la lista; 'key' optimiza el rendimiento identificando cada fila por su ID
                items(
                    items = state.listaElementos,
                    key = { it.id }
                ) { elemento ->
                    TarjetaElemento(
                        elemento = elemento,
                        // Comprueba si el ID de esta fila coincide con el del elemento seleccionado
                        estaSeleccionado = state.elementoSeleccionado?.id == elemento.id,
                        onClick = { onSeleccionarElemento(elemento) }
                    )
                }
            }
        }
    }
}

/**
 * VISTA PREVIA EN ANDROID STUDIO (Panel Split)
 * Permite ver el diseño sin necesidad de arrancar el emulador, pasando datos de prueba a MainPantallaContent.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPreview() {
    val ejemplo1 = Elemento(id = 1, nombre = "Primer elemento")
    val ejemplo2 = Elemento(id = 2, nombre = "Segundo elemento")

    PlantillaExamenComposeTheme {
        MainPantallaContent(
            state = MainState(
                listaElementos = listOf(ejemplo1, ejemplo2),
                elementoSeleccionado = ejemplo2
            )
        )
    }
}