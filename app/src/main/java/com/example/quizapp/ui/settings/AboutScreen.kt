package com.example.quizapp.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.ui.components.AppLogo
import com.example.quizapp.ui.components.AppTitle
import com.example.quizapp.ui.components.ScreenTopBar
import com.example.quizapp.ui.components.SurfaceCard
import com.example.quizapp.ui.theme.AppTheme

const val APP_VERSION = "1.0.0"

@Composable
fun AboutScreen(onBack: () -> Unit) {
    val c = AppTheme.colors
    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "About App", onBack = onBack)
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(24.dp))
            AppLogo(size = 140.dp)
            AppTitle(fontSize = 40.sp)
            Spacer(Modifier.height(8.dp))
            Text("Version $APP_VERSION", color = c.textSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(24.dp))
            Text("Small Steps\nBig Knowledge", color = c.textPrimary, fontSize = 18.sp, lineHeight = 26.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(28.dp))
            SurfaceCard(Modifier.fillMaxWidth()) {
                Text(
                    "Quiz App is an educational app that helps students review Programming, " +
                        "Computer Networks and Computer Science Basics through short multiple-choice quizzes. " +
                        "Get instant feedback on every answer, track your score and time, and review your " +
                        "previous attempts. Everything works offline and is stored on your device.",
                    color = c.textSecondary,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    modifier = Modifier.padding(16.dp),
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
