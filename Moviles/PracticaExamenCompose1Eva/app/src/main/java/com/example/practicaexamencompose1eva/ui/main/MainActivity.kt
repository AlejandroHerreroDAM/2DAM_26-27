package com.example.practicaexamencompose1eva.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicaexamencompose1eva.R
import com.example.practicaexamencompose1eva.di.AppModule
import com.example.practicaexamencompose1eva.domain.model.Alumno
import com.example.practicaexamencompose1eva.ui.componentes.FormularioAlumnoCard
import com.example.practicaexamencompose1eva.ui.componentes.ItemAlumnoCard
import com.example.practicaexamencompose1eva.ui.componentes.ListaAtributosCard
import com.example.practicaexamencompose1eva.ui.componentes.TopBarAlumnos
import com.example.practicaexamencompose1eva.ui.theme.PracticaExamenCompose1EvaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaExamenCompose1EvaTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(
            AppModule.cargarAlumnosUseCase,
            AppModule.addAlumnoUseCase,
            AppModule.delAlumnoUseCase,
            AppModule.updateAlumnoUseCase
        )
    )
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.error) {
        state.error?.let { mensaje ->
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            viewModel.handleErrorMostrado()
        }
    }

    MainScreenContent(
        state = state,
        onSeleccionarAlumno = { alumno -> viewModel.handleSeleccionar(alumno) },
        onEliminarAtributo = { clave -> viewModel.handleEliminarAtributo(clave) },
        onAddAlumno = { nuevoAlumno -> viewModel.handleAdd(nuevoAlumno) },
        onBorrarSeleccionado = { viewModel.handleDelete() }
    )
}

@Composable
fun MainScreenContent(
    state: MainState,
    onSeleccionarAlumno: (Alumno) -> Unit,
    onEliminarAtributo: (String) -> Unit,
    onAddAlumno: (Alumno) -> Unit,
    onBorrarSeleccionado: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopBarAlumnos() },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Lista de alumnos (${state.alumnos.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Lista principal de alumnos
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.alumnos, key = { it.id }) { alumno ->
                    ItemAlumnoCard(
                        alumno = alumno,
                        estaSeleccionado = (alumno.id == state.alumnoSeleccionado?.id),
                        onClick = { onSeleccionarAlumno(alumno) }
                    )
                }
            }

            // Segundo listado con los atributos del alumno seleccionado
            if (state.alumnoSeleccionado != null && state.atributosSeleccionado.isNotEmpty()) {
                ListaAtributosCard(
                    atributos = state.atributosSeleccionado,
                    onAtributoClick = onEliminarAtributo
                )
            }

            // Formulario inferior para añadir nuevo alumno
            FormularioAlumnoCard(
                onAddAlumno = onAddAlumno
            )

            // Botón inferior para borrar el alumno seleccionado
            OutlinedButton(
                onClick = onBorrarSeleccionado,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icono),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Borrar alumno seleccionado")
            }

            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    val alumnoEjemplo = Alumno(
        id = 1,
        nombre = "Lucía",
        apellidos = "Álvarez García",
        curso = "1º DAM",
        edad = 18,
        email = "lucia.alvarez@iesquevedo.es",
        activo = true
    )

    PracticaExamenCompose1EvaTheme {
        MainScreenContent(
            state = MainState(
                alumnos = listOf(
                    alumnoEjemplo,
                    Alumno(
                        id = 2,
                        nombre = "Sofía",
                        apellidos = "Pérez Martín",
                        curso = "1º SMR",
                        edad = 17,
                        email = "sofia.perez@iesquevedo.es",
                        activo = false
                    )
                ),
                alumnoSeleccionado = alumnoEjemplo,
                atributosSeleccionado = alumnoEjemplo.obtenerAtributos()
            ),
            onSeleccionarAlumno = {},
            onEliminarAtributo = {},
            onAddAlumno = {},
            onBorrarSeleccionado = {}
        )
    }
}