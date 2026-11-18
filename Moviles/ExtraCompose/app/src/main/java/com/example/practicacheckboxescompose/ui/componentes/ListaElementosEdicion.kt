package com.example.practicacheckboxescompose.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicacheckboxescompose.domain.model.Tarea

// Vista de edición basada en una lista: pulsa una fila para eliminar ese dato.
@Composable
fun ListaElementosEdicion(
    tarea: Tarea,
    onEliminarTitulo: () -> Unit,
    onEliminarCategoria: () -> Unit,
    onEliminarEstado: () -> Unit,
    onCerrar: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (tarea.titulo.isNotBlank()) {
                item {
                    OutlinedCard(
                        onClick = onEliminarTitulo,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Título", style = MaterialTheme.typography.labelLarge)
                            Text(tarea.titulo, style = MaterialTheme.typography.titleMedium)
                            Text("Pulsa para eliminar", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            if (tarea.categoria.isNotBlank()) {
                item {
                    OutlinedCard(
                        onClick = onEliminarCategoria,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Categoría", style = MaterialTheme.typography.labelLarge)
                            Text(tarea.categoria, style = MaterialTheme.typography.titleMedium)
                            Text("Pulsa para eliminar", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            item {
                OutlinedCard(
                    onClick = onEliminarEstado,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Estado", style = MaterialTheme.typography.labelLarge)
                        Text(
                            if (tarea.completada) "Completada" else "Pendiente",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Pulsa para pasar a pendiente", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        Button(onClick = onCerrar, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text("Terminar edición")
        }
    }
}
