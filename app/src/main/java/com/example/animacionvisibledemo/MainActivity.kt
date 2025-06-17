package com.example.animacionvisibledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.animation.with
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
                    CajaAnimadaConEstados()
                }
            }
        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CajaAnimadaConEstados() {
    var visible by remember { mutableStateOf(true) } // Cambiado a true para que aparezca inicialmente
    var isBlue by remember { mutableStateOf(true) }
    var estado by remember { mutableStateOf<EstadoContenido>(EstadoContenido.CONTENIDO) } // Estado inicial CONTENIDO

    // Animación del color
    val animatedColor by animateColorAsState(
        targetValue = when(estado) {
            EstadoContenido.CARGANDO -> Color.LightGray
            EstadoContenido.CONTENIDO -> if(isBlue) Color.Blue else Color.Green
            EstadoContenido.ERROR -> Color.Red
        },
        animationSpec = tween(durationMillis = 500)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Botón para mostrar/ocultar (AÑADIDO)
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Ocultar Cuadro" else "Mostrar Cuadro")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones para controlar los estados
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { estado = EstadoContenido.CARGANDO }) {
                Text("Cargando")
            }
            Button(onClick = { estado = EstadoContenido.CONTENIDO }) {
                Text("Contenido")
            }
            Button(onClick = { estado = EstadoContenido.ERROR }) {
                Text("Error")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { isBlue = !isBlue }) {
            Text(if (isBlue) "Cambiar a Verde" else "Cambiar a Azul")
        }

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            AnimatedContent(
                targetState = estado,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) with
                            fadeOut(animationSpec = tween(300))
                }
            ) { targetEstado ->
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(animatedColor),
                    contentAlignment = Alignment.Center
                ) {
                    when(targetEstado) {
                        EstadoContenido.CARGANDO -> CircularProgressIndicator(color = Color.White)
                        EstadoContenido.CONTENIDO -> Text("Contenido", color = Color.White)
                        EstadoContenido.ERROR -> Text("¡Error!", color = Color.White)
                    }
                }
            }
        }
    }
}

sealed class EstadoContenido {
    object CARGANDO : EstadoContenido()
    object CONTENIDO : EstadoContenido()
    object ERROR : EstadoContenido()
}