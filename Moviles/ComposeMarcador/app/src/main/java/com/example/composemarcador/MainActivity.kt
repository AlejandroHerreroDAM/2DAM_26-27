package com.example.composemarcador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composemarcador.ui.BarraSuperior
import com.example.composemarcador.ui.BloqueBotonesInferiores
import com.example.composemarcador.ui.BloqueEquipo
import com.example.composemarcador.ui.BloqueFaltas
import com.example.composemarcador.ui.theme.ComposeMarcadorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMarcadorTheme {
                MarcadorPantalla()
            }
        }
    }
}

@Composable
fun MarcadorPantalla(
    viewModel: MainViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    MarcadorPantallaContent(
        state = state,
        onSumarPuntosEquipo1 = { puntos -> viewModel.onSumarPuntosEquipo1(puntos) },
        onSumarPuntosEquipo2 = { puntos -> viewModel.onSumarPuntosEquipo2(puntos) },
        onSumarFaltaEquipo1 = { viewModel.onSumarFaltaEquipo1() },
        onSumarFaltaEquipo2 = { viewModel.onSumarFaltaEquipo2() },
        onEditarClick = { viewModel.onToggleEditar() }
    )
}


@Composable
fun MarcadorPantallaContent(
    state: MainState,
    onSumarPuntosEquipo1: (Int) -> Unit,
    onSumarPuntosEquipo2: (Int) -> Unit,
    onSumarFaltaEquipo1: () -> Unit,
    onSumarFaltaEquipo2: () -> Unit,
    onEditarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = Color.Black,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // 1. Barra superior conectada al estado
            BarraSuperior(
                tiempo = state.tiempo,
                periodo = state.periodo,
                onPlayClick = {}
            )

            // 2. Bloque Equipo 1 conectado al estado y al ViewModel
            BloqueEquipo(
                nombreEquipo = state.nombreEquipo1,
                puntos = state.puntosEquipo1,
                onSumarPuntos = onSumarPuntosEquipo1,
                modoEditar = state.modoEditar
            )

            // 3. Separador central
            Text(
                text = "X",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            // 4. Bloque Equipo 2 conectado al estado y al ViewModel
            BloqueEquipo(
                nombreEquipo = state.nombreEquipo2,
                puntos = state.puntosEquipo2,
                onSumarPuntos = onSumarPuntosEquipo2,
                modoEditar = state.modoEditar
            )

            // 5. Bloque de Faltas conectado al estado y al ViewModel
            BloqueFaltas(
                faltasEquipo1 = state.faltasEquipo1,
                faltasEquipo2 = state.faltasEquipo2,
                onSumarFalta1 = onSumarFaltaEquipo1,
                onSumarFalta2 = onSumarFaltaEquipo2,
                modoEditar = state.modoEditar
            )

            // 6. Botones inferiores
            BloqueBotonesInferiores(
                onEditarClick = onEditarClick,
                onCompartirClick = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MarcadorPreview() {
    ComposeMarcadorTheme {
        MarcadorPantallaContent(
            state = MainState(
                puntosEquipo1 = 34,
                puntosEquipo2 = 18
            ),
            onSumarPuntosEquipo1 = {},
            onSumarPuntosEquipo2 = {},
            onSumarFaltaEquipo1 = {},
            onSumarFaltaEquipo2 = {}
        )
    }
}