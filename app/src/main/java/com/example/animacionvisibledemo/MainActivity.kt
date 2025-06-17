package com.example.animacionvisibledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
                    CajaAnimadaConEstilo()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CajaAnimadaConEstilo() {
    var visible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { visible = !visible },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
        ) {
            Text(
                text = if (visible) "Ocultar Caja" else "Mostrar Caja",
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(500)) +
                    slideInVertically(initialOffsetY = { -200 }, animationSpec = tween(500)) +
                    scaleIn(initialScale = 0.3f, animationSpec = tween(500)) +
                    expandIn(expandFrom = Alignment.Center, animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500)) +
                    slideOutVertically(targetOffsetY = { 200 }, animationSpec = tween(500)) +
                    scaleOut(targetScale = 0.3f, animationSpec = tween(500)) +
                    shrinkOut(shrinkTowards = Alignment.Center, animationSpec = tween(500))
        ) {
            var rotation by remember { mutableStateOf(0f) }

            // Efecto de rotación adicional cuando aparece
            LaunchedEffect(visible) {
                if (visible) rotation = 360f else rotation = 0f
            }

            Box(
                modifier = Modifier
                    .size(180.dp)
                    .rotate(rotation)
                    .background(Color(0xFFE91E63))
            )
        }
    }
}