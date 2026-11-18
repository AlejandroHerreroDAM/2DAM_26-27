package com.example.practicacheckboxescompose.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val categorias = listOf("Trabajo", "Estudios", "Personal", "Urgente")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioTareaCard(onAdd: (String, String) -> Unit) {
    // Estados locales: recuerdan temporalmente el titulo y categoria mientras se escribe.
    var titulo by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf(categorias.first()) }
    var expandido by remember { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Nueva tarea", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Título") },
                    singleLine = true
                )
                ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = !expandido }) {
                    OutlinedTextField(
                        value = categoria,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                        categorias.forEach { opcion ->
                            DropdownMenuItem(text = { Text(opcion) }, onClick = {
                                categoria = opcion
                                expandido = false
                            })
                        }
                    }
                }
            }
            Button(onClick = {
                onAdd(titulo, categoria)
                titulo = ""
            }) {
                Text(" Añadir")
            }
        }
    }
}
