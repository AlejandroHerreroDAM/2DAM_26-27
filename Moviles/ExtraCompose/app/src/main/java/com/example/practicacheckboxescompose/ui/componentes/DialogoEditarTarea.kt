package com.example.practicacheckboxescompose.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.example.practicacheckboxescompose.domain.model.Tarea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoEditarTarea(
    tarea: Tarea,
    onDismiss: () -> Unit,
    onSave: (Tarea) -> Unit
) {
    // Los estados locales empiezan con los datos de la tarea elegida.
    var titulo by remember(tarea) { mutableStateOf(tarea.titulo) }
    var categoria by remember(tarea) { mutableStateOf(tarea.categoria) }
    var expandido by remember { mutableStateOf(false) }
    val categorias = listOf("Trabajo", "Estudios", "Personal", "Urgente")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar tarea") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(titulo, { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
                ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = !expandido }) {
                    OutlinedTextField(
                        value = categoria,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
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
        },
        confirmButton = {
            Button(onClick = { onSave(tarea.copy(titulo = titulo, categoria = categoria)) }) {
                Text("Guardar")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancelar") } }
    )
}
