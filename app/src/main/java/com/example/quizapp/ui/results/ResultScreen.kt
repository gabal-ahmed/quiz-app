package com.example.quizapp.ui.results

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quizapp.data.Category
import com.example.quizapp.data.QuizAttempt
import com.example.quizapp.data.categoryOrNull
import com.example.quizapp.ui.components.PrimaryButton
import com.example.quizapp.ui.components.SecondaryButton
import com.example.quizapp.ui.components.SurfaceCard
import com.example.quizapp.ui.components.formatDate
import com.example.quizapp.ui.components.formatDuration
import com.example.quizapp.ui.theme.AppTheme
import com.example.quizapp.ui.theme.Brand

@Composable
fun ResultScreen(
    viewModel: ResultViewModel,
    onTryAgain: (Category) -> Unit,
    onBackToHome: (QuizAttempt) -> Unit,
) {
    val attempt by viewModel.attempt.collectAsStateWithLifecycle()
    val current = attempt
    if (current == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Brand.Purple)
        }
        return
    }
    val c = AppTheme.colors
    val category = current.categoryOrNull()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(110.dp),
            contentAlignment = Alignment.Center,
        ) {
            Confetti(Modifier.fillMaxSize())
            Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Brand.Gold, modifier = Modifier.size(84.dp))
        }
        Text("Quiz Completed!", color = c.textPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Your Score", color = c.textSecondary, fontSize = 14.sp)
        Text(
            "${current.correctAnswers}/${current.totalQuestions}",
            color = c.textPrimary,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(12.dp))
        AccuracyRing(percentage = current.scorePercentage, size = 128.dp)

        Spacer(Modifier.height(20.dp))
        SurfaceCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                DetailRow("Category", category?.title ?: current.category)
                DetailRow("Total Questions", current.totalQuestions.toString())
                DetailRow("Correct Answers", current.correctAnswers.toString())
                DetailRow("Wrong Answers", (current.totalQuestions - current.correctAnswers).toString())
                DetailRow("Time Taken", formatDuration(current.durationSeconds))
                DetailRow("Date", formatDate(current.completedAt))
            }
        }

        Spacer(Modifier.height(20.dp))
        if (category != null) {
            PrimaryButton("Try Again", onClick = { onTryAgain(category) }, icon = Icons.Filled.Refresh)
            Spacer(Modifier.height(12.dp))
        }
        SecondaryButton("Back to Home", onClick = { onBackToHome(current) }, icon = Icons.Outlined.Home)
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    val c = AppTheme.colors
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(label, color = c.textSecondary, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(value, color = c.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun AccuracyRing(percentage: Int, size: Dp) {
    val c = AppTheme.colors
    val animated = remember { Animatable(0f) }
    LaunchedEffect(percentage) { animated.animateTo(percentage.toFloat(), tween(900)) }

    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 12.dp.toPx()
            val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
            val topLeft = Offset(stroke / 2, stroke / 2)
            drawArc(c.border, 0f, 360f, useCenter = false, topLeft = topLeft, size = arcSize, style = Stroke(stroke))
            drawArc(
                brush = Brush.sweepGradient(listOf(Brand.Correct, Brand.Blue, Brand.Purple, Brand.Correct)),
                startAngle = -90f,
                sweepAngle = 360f * animated.value / 100f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(stroke, cap = StrokeCap.Round),
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${animated.value.toInt()}%", color = c.textPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("Accuracy", color = c.textSecondary, fontSize = 12.sp)
        }
    }
}

private data class ConfettiPiece(val x: Float, val y: Float, val angle: Float, val color: Color)

private val ConfettiPieces = listOf(
    ConfettiPiece(0.18f, 0.25f, 20f, Brand.Gold),
    ConfettiPiece(0.28f, 0.60f, -30f, Brand.Correct),
    ConfettiPiece(0.24f, 0.85f, 45f, Brand.Incorrect),
    ConfettiPiece(0.34f, 0.15f, 70f, Color(0xFF38BDF8)),
    ConfettiPiece(0.66f, 0.15f, -60f, Brand.Incorrect),
    ConfettiPiece(0.74f, 0.55f, 25f, Brand.PurpleLight),
    ConfettiPiece(0.82f, 0.30f, -15f, Brand.Correct),
    ConfettiPiece(0.78f, 0.85f, 60f, Brand.Gold),
)

@Composable
private fun Confetti(modifier: Modifier) {
    Canvas(modifier) {
        val w = 6.dp.toPx()
        val h = 14.dp.toPx()
        ConfettiPieces.forEach { piece ->
            val center = Offset(size.width * piece.x, size.height * piece.y)
            rotate(piece.angle, pivot = center) {
                drawRoundRect(
                    color = piece.color,
                    topLeft = Offset(center.x - w / 2, center.y - h / 2),
                    size = Size(w, h),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(w / 2),
                )
            }
        }
    }
}
