package com.example.animacionvisibledemo.iu

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Habilita APIs experimentales de animación
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimacionesCombinadasScreen() {
    // Variables de estado para controlar la lógica visual
    var isExpanded by remember { mutableStateOf(false) }       // Controla si el cuadro se expande
    var isButtonVisible by remember { mutableStateOf(true) }   // Controla si el botón está visible
    var isDarkMode by remember { mutableStateOf(false) }       // Controla si el modo oscuro está activado

    // Variables para personalizar el tipo de animación
    var animationType by remember { mutableStateOf(1) }        // Tipo de animación: 1=spring, 2=tween, 3=repeatable
    var duration by remember { mutableStateOf(1000) }          // Duración de la animación en milisegundos
    var dampingRatio by remember { mutableStateOf(0.5f) }      // Relación de amortiguamiento para spring
    var stiffness by remember { mutableStateOf(Spring.StiffnessLow.toFloat()) } // Rigidez para spring
    var iterations by remember { mutableStateOf(3) }           // Cantidad de repeticiones si es repeatable

    // Animación de tamaño configurable
    val animatedSize by animateDpAsState(
        targetValue = if (isExpanded) 200.dp else 100.dp,      // Si está expandido: 200.dp, si no: 100.dp
        animationSpec = when (animationType) {
            1 -> spring(dampingRatio = dampingRatio, stiffness = stiffness)         // Resorte físico
            2 -> tween(durationMillis = duration, easing = FastOutSlowInEasing)     // Animación suavizada
            3 -> repeatable(                                                        // Animación repetitiva
                iterations = iterations,
                animation = tween(durationMillis = duration),
                repeatMode = RepeatMode.Reverse
            )
            else -> spring()
        },
        label = ""  // Etiqueta opcional (usada para depuración)
    )

    // Animación de color cuando se expande o contrae el cuadro
    val animatedColor by animateColorAsState(
        targetValue = if (isExpanded) Color(0xFF6200EE) else Color(0xFF03DAC6),
        animationSpec = tween(durationMillis = duration),
        label = ""
    )

    // Cambios de fondo según modo oscuro o claro
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF5F5F5)
    val contentColor = if (isDarkMode) Color.White else Color.Black

    // Contenedor general de toda la pantalla
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Panel de configuración de la animación
            Column {
                Text("Configuración de Animación", color = contentColor)
                Spacer(modifier = Modifier.height(8.dp))

                // Botones para elegir el tipo de animación
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Tipo:", color = contentColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { animationType = 1 }, modifier = Modifier.weight(1f)) {
                        Text("Spring")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { animationType = 2 }, modifier = Modifier.weight(1f)) {
                        Text("Tween")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { animationType = 3 }, modifier = Modifier.weight(1f)) {
                        Text("Repeat")
                    }
                }

                // Si se eligió "Spring", se muestra configuración adicional
                if (animationType == 1) {
                    Column {
                        Text("Damping Ratio: $dampingRatio", color = contentColor)
                        Slider(
                            value = dampingRatio,
                            onValueChange = { dampingRatio = it },
                            valueRange = 0.1f..1f
                        )

                        Text("Stiffness: ${stiffness.toInt()}", color = contentColor)
                        Slider(
                            value = stiffness,
                            onValueChange = { stiffness = it },
                            valueRange = 10f..1000f
                        )
                    }
                }

                // Control para modificar la duración de la animación
                Text("Duración: ${duration}ms", color = contentColor)
                Slider(
                    value = duration.toFloat(),
                    onValueChange = { duration = it.toInt() },
                    valueRange = 100f..3000f
                )

                // Si se eligió "Repeat", se configura el número de repeticiones
                if (animationType == 3) {
                    Text("Iteraciones: $iterations", color = contentColor)
                    Slider(
                        value = iterations.toFloat(),
                        onValueChange = { iterations = it.toInt() },
                        valueRange = 1f..10f
                    )
                }
            }

            // Cuadro animado que cambia de tamaño y color
            Box(
                modifier = Modifier
                    .size(animatedSize)
                    .clip(RoundedCornerShape(16.dp))
                    .background(animatedColor)
                    .clickable { isExpanded = !isExpanded },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isExpanded) "Contraer" else "Expandir",
                    color = Color.White
                )
            }

            // Botón que aparece y desaparece con animación
            AnimatedVisibility(
                visible = isButtonVisible,
                enter = slideInVertically(animationSpec = tween(duration)) + fadeIn(),
                exit = slideOutHorizontally(animationSpec = tween(duration)) + fadeOut()
            ) {
                Button(
                    onClick = { isButtonVisible = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF5722),
                        contentColor = Color.White
                    )
                ) {
                    Text("Ocultar este botón")
                }
            }

            // Botón para cambiar entre modo oscuro y claro, con animación de transición
            AnimatedContent(
                targetState = isDarkMode,
                transitionSpec = {
                    fadeIn(animationSpec = tween(duration)) with
                            fadeOut(animationSpec = tween(duration))
                },
                label = ""
            ) { darkMode ->
                Button(
                    onClick = { isDarkMode = !darkMode },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (darkMode) Color(0xFFBB86FC) else Color(0xFF3700B3),
                        contentColor = Color.White
                    )
                ) {
                    Text(if (darkMode) "Cambiar a Modo Claro" else "Cambiar a Modo Oscuro")
                }
            }

            // Botón para reiniciar todas las animaciones
            Button(onClick = {
                isButtonVisible = true
                isExpanded = false
            }) {
                Text("Resetear Animaciones")
            }
        }
    }
}
