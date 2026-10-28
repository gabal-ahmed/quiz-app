package com.example.quizapp.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.ui.components.AppLogo
import com.example.quizapp.ui.components.AppTitle
import com.example.quizapp.ui.theme.AppTheme
import com.example.quizapp.ui.theme.Brand

/** Shows the brand briefly, then routes to Home or Login depending on the saved session. */
@Composable
fun SplashScreen(isLoggedIn: suspend () -> Boolean, onFinished: (loggedIn: Boolean) -> Unit) {
    val c = AppTheme.colors
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(durationMillis = 1200, easing = LinearEasing))
        onFinished(isLoggedIn())
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AppLogo(size = 140.dp)
        Spacer(Modifier.height(16.dp))
        AppTitle(fontSize = 44.sp)
        Spacer(Modifier.height(12.dp))
        Text(
            "Test Your Knowledge\nImprove Your Skills!",
            color = c.textPrimary,
            fontSize = 17.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(56.dp))
        LinearProgressIndicator(
            progress = { progress.value },
            modifier = Modifier
                .width(140.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Brand.Purple,
            trackColor = c.border,
            drawStopIndicator = {},
        )
    }
}
