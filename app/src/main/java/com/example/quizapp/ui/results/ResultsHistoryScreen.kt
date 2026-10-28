package com.example.quizapp.ui.results

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quizapp.data.Category
import com.example.quizapp.data.QuizAttempt
import com.example.quizapp.data.categoryOrNull
import com.example.quizapp.ui.components.BestScoreCard
import com.example.quizapp.ui.components.CategoryBadge
import com.example.quizapp.ui.components.ScreenTopBar
import com.example.quizapp.ui.components.SurfaceCard
import com.example.quizapp.ui.components.formatDate
import com.example.quizapp.ui.theme.AppTheme
import com.example.quizapp.ui.theme.Brand

@Composable
fun ResultsHistoryScreen(
    viewModel: HistoryViewModel,
    onAttemptClick: (QuizAttempt) -> Unit,
) {
    val c = AppTheme.colors
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(
            title = "Your Results",
            leading = { Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Brand.Gold) },
        )
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                BestScoreCard(
                    best = state.best,
                    title = if (state.filter == null) "Best Score" else "Best Score • ${state.filter?.shortTitle}",
                )
            }
            item {
                FilterRow(selected = state.filter, onSelect = viewModel::setFilter)
            }
            if (!state.isLoading && state.attempts.isEmpty()) {
                item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(Icons.Outlined.BarChart, contentDescription = null, tint = c.textSecondary, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("No results yet", color = c.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            "Complete a quiz and your attempts will appear here.",
                            color = c.textSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            items(state.attempts, key = { it.id }) { attempt ->
                AttemptRow(attempt, onClick = { onAttemptClick(attempt) })
            }
        }
    }
}

@Composable
private fun FilterRow(selected: Category?, onSelect: (Category?) -> Unit) {
    val options: List<Pair<String, Category?>> = listOf("All" to null) + Category.entries.map { it.shortTitle to it }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options) { (label, category) ->
            FilterPill(label = label, selected = selected == category, onClick = { onSelect(category) })
        }
    }
}

@Composable
private fun FilterPill(label: String, selected: Boolean, onClick: () -> Unit) {
    val c = AppTheme.colors
    val shape = RoundedCornerShape(50)
    Box(
        Modifier
            .clip(shape)
            .background(if (selected) Brand.Blue else c.surface)
            .border(1.dp, if (selected) Brand.Blue else c.border, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(label, color = if (selected) Color.White else c.textSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun AttemptRow(attempt: QuizAttempt, onClick: () -> Unit) {
    val c = AppTheme.colors
    val category = attempt.categoryOrNull()
    SurfaceCard(Modifier.fillMaxWidth(), onClick = onClick) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            if (category != null) CategoryBadge(category, size = 42.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(category?.title ?: attempt.category, color = c.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(formatDate(attempt.completedAt), color = c.textSecondary, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${attempt.correctAnswers}/${attempt.totalQuestions}", color = c.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("${attempt.scorePercentage}%", color = if (attempt.scorePercentage >= 50) Brand.Correct else Brand.Incorrect, fontSize = 12.sp)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = c.textSecondary)
        }
    }
}
