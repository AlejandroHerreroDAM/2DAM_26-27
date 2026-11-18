package com.example.practicaexamencompose1eva.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicaexamencompose1eva.domain.model.Alumno

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAlumnoCard(
    onAddAlumno: (Alumno) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var curso by remember { mutableStateOf("1º DAM") }
    var edad by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }
    var menuCursoExpandido by remember { mutableStateOf(false) }

    val cursos = listOf("1º DAM", "2º DAM", "1º ASIR", "2º ASIR", "1º SMR", "2º SMR")
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface
    )

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Añadir nuevo alumno",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Fila 1: Nombre y Apellidos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CampoFormulario(
                    etiqueta = "Nombre",
                    valor = nombre,
                    placeholder = "Nombre",
                    onValueChange = { nombre = it },
                    modifier = Modifier.weight(1f),
                    colors = fieldColors
                )
                CampoFormulario(
                    etiqueta = "Apellidos",
                    valor = apellidos,
                    placeholder = "Apellidos",
                    onValueChange = { apellidos = it },
                    modifier = Modifier.weight(1f),
                    colors = fieldColors
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Fila 2: Curso (Desplegable) y Edad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Curso",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    ExposedDropdownMenuBox(
                        expanded = menuCursoExpandido,
                        onExpandedChange = { menuCursoExpandido = !menuCursoExpandido }
                    ) {
                        OutlinedTextField(
                            value = curso,
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 13.sp),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuCursoExpandido)
                            },
                            colors = fieldColors,
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = menuCursoExpandido,
                            onDismissRequest = { menuCursoExpandido = false }
                        ) {
                            cursos.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion, fontSize = 13.sp) },
                                    onClick = {
                                        curso = opcion
                                        menuCursoExpandido = false
                                    }
                                )
                            }
                        }
                    }
                }

                CampoFormulario(
                    etiqueta = "Edad",
                    valor = edad,
                    placeholder = "Edad",
                    onValueChange = { if (it.length <= 2) edad = it },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f),
                    colors = fieldColors
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Fila 3: Email y Checkbox Activo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                CampoFormulario(
                    etiqueta = "Email",
                    valor = email,
                    placeholder = "correo@ejemplo.es",
                    onValueChange = { email = it },
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.weight(1f),
                    colors = fieldColors
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Activo",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = activo,
                            onCheckedChange = { activo = it }
                        )
                        Text(
                            text = "Alumno activo",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Botón Añadir alumno
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        val nuevo = Alumno(
                            nombre = nombre,
                            apellidos = apellidos,
                            curso = curso,
                            edad = edad.toIntOrNull() ?: 0,
                            email = email,
                            activo = activo
                        )
                        onAddAlumno(nuevo)
                        nombre = ""
                        apellidos = ""
                        curso = "1º DAM"
                        edad = ""
                        email = ""
                        activo = true
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "+ Añadir alumno", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun CampoFormulario(
    etiqueta: String,
    valor: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    colors: androidx.compose.material3.TextFieldColors
) {
    Column(modifier = modifier) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(fontSize = 13.sp),
            placeholder = { Text(text = placeholder, fontSize = 12.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = colors,
            modifier = Modifier.fillMaxWidth()
        )
    }
}