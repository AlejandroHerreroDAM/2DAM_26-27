package com.example.composemarcador.ui


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composemarcador.R

val DigitalFont = FontFamily(
    Font(R.font.digital_7)
)

@Composable
fun NumeroDigital(
    texto: String,
    color: Color,
    tamano: TextUnit,
) {
    Text(
        text= texto,
        color= color,
        fontSize = tamano,
        fontFamily = DigitalFont,
    )
}

@Composable
fun BotonCircular(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tamano: Dp = 52.dp
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.size(tamano)
    ) {
        Text(
            text = texto,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BloqueEquipo(
    nombreEquipo: String,
    puntos: Int,
    onSumarPuntos: (Int) -> Unit,
    modoEditar: Boolean = false,
    modifier: Modifier = Modifier
) {
    val signo = if (modoEditar) "-" else "+"

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            BotonCircular(texto = "${signo}1", onClick = { onSumarPuntos(1) })
            BotonCircular(texto = "${signo}2", onClick = { onSumarPuntos(2) })
            BotonCircular(texto = "${signo}3", onClick = { onSumarPuntos(3) })
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = nombreEquipo,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            NumeroDigital(
                texto = puntos.toString(),
                color = Color.Green,
                tamano = 100.sp
            )
        }
    }
}

@Composable
fun BarraSuperior(
  tiempo: String,
  periodo: String,
  onPlayClick: () -> Unit,
  modifier: Modifier = Modifier,
){
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ){
        IconButton(
            onClick = onPlayClick,
            modifier = Modifier.size(56.dp).border(
                width=3.dp,
                color= Color.White,
                shape = CircleShape,
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_play),
                contentDescription = "Iniciar o pausar reloj",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        NumeroDigital(
            texto = tiempo,
            color = Color.Red,
            tamano = 56.sp,
        )

        Text(
            text = periodo,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BloqueFaltas(
    faltasEquipo1: Int,
    faltasEquipo2: Int,
    onSumarFalta1: () -> Unit,
    onSumarFalta2: () -> Unit,
    modoEditar: Boolean = false,
    modifier: Modifier = Modifier,
 ){
    val signo = if (modoEditar) "-" else "+"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Faltas",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            BotonCircular(
                texto = "${signo}",
                onClick = {
                    onSumarFalta1()
                }
            )

            NumeroDigital(
                texto = faltasEquipo1.toString(),
                color = Color.Green,
                tamano = 50.sp,
            )

            Text(
                text = "X",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            NumeroDigital(
                texto = faltasEquipo2.toString(),
                color = Color.Green,
                tamano = 50.sp,
            )

            BotonCircular(
                texto = "${signo}",
                onClick = {
                    onSumarFalta2()
                }
            )
        }
    }
}

@Composable
fun BotonAccion(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(12.dp),
        modifier = modifier
    ) {
        Text(
            text = texto,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BloqueBotonesInferiores(
    onEditarClick: () -> Unit,
    onCompartirClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(30.dp),
    ) {
        BotonAccion(texto = "EDITAR", onClick = onEditarClick)
        BotonAccion(texto = "COMPARTIR", onClick = onCompartirClick)
    }
}