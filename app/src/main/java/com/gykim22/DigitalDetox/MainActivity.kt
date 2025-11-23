package com.gykim22.DigitalDetox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gykim22.DigitalDetox.Core.NavGraph
import com.gykim22.DigitalDetox.Core.Screen
import com.gykim22.DigitalDetox.ui.theme.DigitalDetoxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DigitalDetoxTheme {
                NavGraph(
                    startDestination = Screen.TimerScreen.route
                )
            }
        }
    }
}