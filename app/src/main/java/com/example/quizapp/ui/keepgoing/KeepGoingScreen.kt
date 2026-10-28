package com.example.quizapp.ui.keepgoing

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.ui.components.PrimaryButton
import com.example.quizapp.ui.components.SurfaceCard
import com.example.quizapp.ui.theme.AppTheme
import com.example.quizapp.ui.theme.Brand

/** Optional motivational screen, shown after a result below the pass mark. */
@Composable
fun KeepGoingScreen(onBackToHome: () -> Unit) {
    val c = AppTheme.colors
    BackHandler(onBack = onBackToHome)

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.height(24.dp))
        Box(Modifier.size(200.dp), contentAlignment = Alignment.Center) {
            Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = Brand.Purple, modifier = Modifier.size(140.dp))
            Icon(
                Icons.Filled.School,
                contentDescription = null,
                tint = Brand.Blue,
                modifier = Modifier.size(84.dp).align(Alignment.TopCenter).offset(x = (-16).dp),
            )
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Brand.Correct,
                modifier = Modifier.size(52.dp).align(Alignment.BottomEnd).offset(x = (-12).dp, y = (-24).dp),
            )
            Icon(Icons.Filled.Star, contentDescription = null, tint = Brand.Gold, modifier = Modifier.size(28.dp).align(Alignment.TopStart))
            Icon(Icons.Filled.Star, contentDescription = null, tint = Brand.Incorrect, modifier = Modifier.size(18.dp).align(Alignment.CenterEnd))
        }

        Spacer(Modifier.height(16.dp))
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(color = c.textPrimary)) { append("Keep ") }
                withStyle(SpanStyle(color = AppTheme.colors.accent)) { append("Going!") }
            },
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Every quiz is a step closer to your goals.\nYou've got this! 🚀",
            color = c.textSecondary,
            fontSize = 16.sp,
            lineHeight = 23.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(24.dp))
        SurfaceCard(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = Brand.Gold, modifier = Modifier.size(32.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("“Knowledge is power.”", color = c.textPrimary, fontSize = 16.sp, fontStyle = FontStyle.Italic)
                    Text("— Francis Bacon", color = c.textSecondary, fontSize = 12.sp)
                }
            }
        }

        Spacer(Modifier.height(28.dp))
        PrimaryButton("Back to Home", onClick = onBackToHome, icon = Icons.Outlined.Home)
    }
}
