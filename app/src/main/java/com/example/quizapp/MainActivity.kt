package com.example.quizapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quizapp.navigation.AppNavigation
import com.example.quizapp.ui.components.AppBackground
import com.example.quizapp.ui.theme.QuizAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as QuizApplication).container

        setContent {
            val darkMode by container.settings.darkMode.collectAsStateWithLifecycle(initialValue = true)

            DisposableEffect(darkMode) {
                val style = if (darkMode) {
                    SystemBarStyle.dark(Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                }
                enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
                onDispose {}
            }

            QuizAppTheme(darkTheme = darkMode) {
                AppBackground {
                    AppNavigation(container)
                }
            }
        }
    }
}
