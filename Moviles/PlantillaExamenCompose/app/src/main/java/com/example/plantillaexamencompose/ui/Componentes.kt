package com.example.plantillaexamencompose.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantillaexamencompose.model.Elemento

/**
 * BOTÓN REUTILIZABLE
 * Recibe el texto, la acción al pulsar (lambda) y opcionalmente un color de fondo distinto.
 */
@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorFondo: Color = MaterialTheme.colorScheme.primary
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorFondo),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        modifier = modifier
    ) {
        Text(
            text = texto,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * CAMPO DE TEXTO REUTILIZABLE (Equivalente a EditText)
 * Funciona con "State Hoisting": no guarda el texto dentro, sino que muestra 'valor'
 * y avisa hacia arriba cada vez que cambia mediante 'onValorChange'.
 */
@Composable
fun CampoTexto(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(text = etiqueta) },
        singleLine = true, // Evita que al pulsar Enter crezca hacia abajo en varias líneas
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * FILA / TARJETA DE LA LISTA (Equivalente al item_layout.xml de un RecyclerView)
 * Pinta un objeto 'Elemento' y cambia su color y borde si 'estaSeleccionado' es true.
 */
@Composable
fun TarjetaElemento(
    elemento: Elemento,
    estaSeleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Cambiamos el fondo dinámicamente según si esta tarjeta es la seleccionada
    val colorTarjeta = if (estaSeleccionado) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    // Añadimos un borde resaltado solo cuando está seleccionada
    val bordeTarjeta = if (estaSeleccionado) {
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    } else {
        null
    }

    Card(
        onClick = onClick, // Hace que toda la tarjeta sea pulsable
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorTarjeta),
        border = bordeTarjeta,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = elemento.nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (estaSeleccionado) "Seleccionado (pulsa BORRAR para eliminar)" else "ID #${elemento.id}",
                fontSize = 13.sp,
                color = if (estaSeleccionado) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}