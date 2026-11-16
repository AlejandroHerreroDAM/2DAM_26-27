package com.example.practicacompose_alejandroherrero.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.practicacompose_alejandroherrero.domain.model.Alumno
import com.example.practicacompose_alejandroherrero.ui.main.MainState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAlumnos(
    state: MainState,
    onChangeAlumno: (Alumno) -> Unit,
    onAddAlumno: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Añadir nuevo alumno",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(Modifier.weight(1F)) {
                    Text(
                        text = "Nombre",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    OutlinedTextField(
                        value = state.alumno.nombre,
                        onValueChange = { onChangeAlumno(state.alumno.copy(nombre = it)) },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                Column(Modifier.weight(1F)) {
                    Text(
                        text = "Apellido",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    OutlinedTextField(
                        value = state.alumno.apellido,
                        onValueChange = { onChangeAlumno(state.alumno.copy(apellido = it)) },
                        label = { Text("Apellido") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(Modifier.weight(1F)) {
                    Text(
                        text = "Curso",
                        style = MaterialTheme.typography.bodySmall
                    )

                    OutlinedTextField(
                        value = state.alumno.curso,
                        onValueChange = { onChangeAlumno(state.alumno.copy(curso = it)) },
                        label = { Text("1ºDAM") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                Column(Modifier.weight(1F)) {
                    Text(
                        text = "Edad",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    OutlinedTextField(
                        value = if (state.alumno.edad == 0) "" else state.alumno.edad.toString(),
                        onValueChange = { texto ->
                            onChangeAlumno(state.alumno.copy(edad = texto.toIntOrNull() ?: 0))
                        },
                        label = { Text("Edad") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(Modifier.weight(1F)) {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    OutlinedTextField(
                        value = state.alumno.correo,
                        onValueChange = { onChangeAlumno(state.alumno.copy(correo = it)) },
                        label = { Text("correo@ej.es") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                }
                Column(Modifier.weight(1F)) {
                    AlumnoCheckbox(
                        checked = state.alumno.activo,
                        onCheckedChange = { onChangeAlumno(state.alumno.copy(activo = it)) }
                    )
                    Button(
                        onClick = onAddAlumno,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(Modifier.width(10.dp))
                        Text("+ Añadir Alumno")
                    }
                }
            }

        }
    }
}

@Composable
fun AlumnoCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = if (checked) "Activo" else "Inactivo")
    }
}