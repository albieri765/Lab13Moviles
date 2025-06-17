package com.example.animacionvisibledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animacionvisibledemo.ui.theme.AnimacionVisibleDemoTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimacionVisibleDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CajaAnimadaConMovimiento()
                }
            }
        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CajaAnimadaConMovimiento() {
    var visible by remember { mutableStateOf(false) }
    var isBlue by remember { mutableStateOf(true) }
    // Nuevos estados para posición y tamaño
    var isMoved by remember { mutableStateOf(false) }

    // Animaciones
    val animatedColor by animateColorAsState(
        targetValue = if (isBlue) Color.Blue else Color.Green,
        animationSpec = tween(durationMillis = 1000)
    )

    val animatedSize by animateDpAsState(
        targetValue = if (isMoved) 120.dp else 180.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    val animatedOffsetX by animateDpAsState(
        targetValue = if (isMoved) 100.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val animatedOffsetY by animateDpAsState(
        targetValue = if (isMoved) (-50).dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Ocultar Caja" else "Mostrar Caja")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { isBlue = !isBlue }) {
            Text(if (isBlue) "Cambiar a Verde" else "Cambiar a Azul")
        }

        // Nuevo botón para mover y cambiar tamaño
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { isMoved = !isMoved },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
        ) {
            Text("Mover y Redimensionar")
        }

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically() + scaleIn(),
            exit = fadeOut() + slideOutVertically() + scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .size(animatedSize) // Tamaño animado
                    .offset(x = animatedOffsetX, y = animatedOffsetY) // Posición animada
                    .background(animatedColor)
            )
        }
    }
}