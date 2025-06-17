package com.example.animacionvisibledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.animacionvisibledemo.iu.AnimacionesCombinadasScreen
import com.example.animacionvisibledemo.ui.theme.AnimacionVisibleDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimacionVisibleDemoTheme {
                AnimacionesCombinadasScreen()
            }
        }
    }
}
