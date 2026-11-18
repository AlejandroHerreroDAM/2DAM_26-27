package com.example.practicacheckboxescompose.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.practicacheckboxescompose.domain.model.Tarea

@Composable
fun ItemTareaCard(
    tarea: Tarea,
    marcada: Boolean,
    onCheck: () -> Unit,
    onToggleCompletada: () -> Unit,
    onEdit: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    // Cuando esta marcada cambia el contenedor y el borde; los colores vienen del tema.
    OutlinedCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (marcada) scheme.primaryContainer else scheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (marcada) 2.dp else 1.dp,
            if (marcada) scheme.primary else scheme.outline
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(checked = marcada, onCheckedChange = { onCheck() })
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    tarea.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(tarea.categoria, style = MaterialTheme.typography.bodyMedium)
            }
            AssistChip(
                onClick = onToggleCompletada,
                label = { Text(if (tarea.completada) "Completada" else "Pendiente") },
                colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
                    containerColor = if (tarea.completada) scheme.tertiaryContainer else scheme.surfaceVariant,
                    labelColor = if (tarea.completada) scheme.onTertiaryContainer else scheme.onSurfaceVariant
                )
            )
            androidx.compose.material3.TextButton(onClick = onEdit) {
                Text("Editar")
            }
        }
    }
}
