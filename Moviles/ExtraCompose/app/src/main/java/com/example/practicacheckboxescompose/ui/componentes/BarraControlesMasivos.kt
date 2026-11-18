package com.example.practicacheckboxescompose.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicacheckboxescompose.ui.main.FiltroTarea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraControlesMasivos(
    textoBusqueda: String,
    filtro: FiltroTarea,
    cantidadMarcadas: Int,
    todasVisiblesMarcadas: Boolean,
    onBusquedaChange: (String) -> Unit,
    onFiltroChange: (FiltroTarea) -> Unit,
    onMarcarTodas: () -> Unit,
    onCompletar: () -> Unit,
    onBorrar: () -> Unit
) {
    // Agrupa busqueda, filtros y operaciones sobre las tareas seleccionadas.
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(12.dp)) {
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = onBusquedaChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar por título o categoría") },
            singleLine = true,
            trailingIcon = {
                if (textoBusqueda.isNotEmpty()) {
                    androidx.compose.material3.TextButton(onClick = { onBusquedaChange("") }) {
                        Text("Limpiar")
                    }
                }
            }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FiltroTarea.entries.forEach { opcion ->
                FilterChip(
                    selected = filtro == opcion,
                    onClick = { onFiltroChange(opcion) },
                    label = {
                        Text(
                            when (opcion) {
                                FiltroTarea.TODAS -> "Todas"
                                FiltroTarea.PENDIENTES -> "Pendientes"
                                FiltroTarea.COMPLETADAS -> "Completadas"
                            }
                        )
                    }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            FilledTonalButton(onClick = onMarcarTodas, modifier = Modifier.weight(1f)) {
                Text(if (todasVisiblesMarcadas) "Desmarcar todas" else "Marcar todas")
            }
            Button(onClick = onCompletar, enabled = cantidadMarcadas > 0, modifier = Modifier.weight(1f)) {
                Text("Completar ($cantidadMarcadas)")
            }
            OutlinedButton(
                onClick = onBorrar,
                enabled = cantidadMarcadas > 0,
                modifier = Modifier.weight(1f),
                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Borrar ($cantidadMarcadas)")
            }
        }
    }
}
