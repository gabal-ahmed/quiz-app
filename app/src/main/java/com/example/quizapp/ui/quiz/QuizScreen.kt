package com.example.quizapp.ui.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quizapp.data.Question
import com.example.quizapp.ui.components.CategoryBadge
import com.example.quizapp.ui.components.ConfirmDialog
import com.example.quizapp.ui.components.PrimaryButton
import com.example.quizapp.ui.components.ScreenTopBar
import com.example.quizapp.ui.theme.AppTheme
import com.example.quizapp.ui.theme.Brand

private val OptionLetters = listOf("A", "B", "C", "D")

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onQuizFinished: (attemptId: Long) -> Unit,
    onExit: () -> Unit,
) {
    val c = AppTheme.colors
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showExitDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state.completedAttemptId) {
        state.completedAttemptId?.let(onQuizFinished)
    }
    BackHandler { showExitDialog = true }

    val question = state.currentQuestion ?: return
    val total = state.questions.size
    val progress by animateFloatAsState(state.progress, label = "progress")

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(
            title = state.category.title,
            subtitle = "$total questions • ${state.category.difficulty} difficulty",
            onBack = { showExitDialog = true },
            leading = { CategoryBadge(state.category, size = 40.dp) },
        )

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Brand.Purple,
                    trackColor = c.border,
                    drawStopIndicator = {},
                )
                Spacer(Modifier.width(12.dp))
                Text("${state.currentIndex + 1}/$total", color = c.textSecondary, fontSize = 13.sp)
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "Question ${state.currentIndex + 1} of $total",
                color = AppTheme.colors.accent,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(8.dp))
            QuestionCard(question)

            Spacer(Modifier.height(16.dp))
            question.options.forEachIndexed { index, option ->
                OptionItem(
                    letter = OptionLetters[index],
                    text = option,
                    state = optionState(state, question, index),
                    onClick = { viewModel.selectAnswer(index) },
                )
                Spacer(Modifier.height(10.dp))
            }
            Spacer(Modifier.height(8.dp))
        }

        PrimaryButton(
            text = if (state.isLastQuestion) "Finish" else "Next",
            onClick = viewModel::next,
            enabled = state.isAnswered,
            loading = state.isSaving,
            modifier = Modifier.padding(20.dp),
        )
    }

    if (state.showWrongAnswer) {
        AnswerFeedbackDialog(
            correctLetter = OptionLetters[question.correctAnswerIndex],
            correctAnswer = question.correctAnswer,
            isLastQuestion = state.isLastQuestion,
            onNext = viewModel::next,
        )
    }

    if (showExitDialog) {
        ConfirmDialog(
            title = "Leave quiz?",
            message = "Your progress in this attempt will be lost and it won't be saved.",
            confirmText = "Leave",
            destructive = true,
            onConfirm = {
                showExitDialog = false
                onExit()
            },
            onDismiss = { showExitDialog = false },
        )
    }
}

@Composable
private fun QuestionCard(question: Question) {
    val c = AppTheme.colors
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(c.surface)
            .border(1.dp, c.border, RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Text(question.text, color = c.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, lineHeight = 24.sp)
        if (question.code != null) {
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(c.codeBackground)
                    .horizontalScroll(rememberScrollState())
                    .padding(14.dp),
            ) {
                Text(question.code, color = c.textPrimary, fontFamily = FontFamily.Monospace, fontSize = 14.sp, lineHeight = 20.sp)
            }
        }
    }
}

private enum class OptionState { Idle, Correct, Wrong, Dimmed }

private fun optionState(state: QuizUiState, question: Question, index: Int): OptionState = when {
    !state.isAnswered -> OptionState.Idle
    index == question.correctAnswerIndex -> OptionState.Correct
    index == state.selectedIndex -> OptionState.Wrong
    else -> OptionState.Dimmed
}

@Composable
private fun OptionItem(letter: String, text: String, state: OptionState, onClick: () -> Unit) {
    val c = AppTheme.colors
    val shape = RoundedCornerShape(14.dp)
    val (background, border, content) = when (state) {
        OptionState.Idle -> Triple(c.surface, c.border, c.textPrimary)
        OptionState.Correct -> Triple(Brand.Correct, Brand.Correct, Color.White)
        OptionState.Wrong -> Triple(Brand.Incorrect, Brand.Incorrect, Color.White)
        OptionState.Dimmed -> Triple(c.surface.copy(alpha = 0.6f), c.border, c.textSecondary)
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background)
            .border(1.dp, border, shape)
            .clickable(enabled = state == OptionState.Idle, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (state == OptionState.Idle || state == OptionState.Dimmed) c.surfaceHigh else Color.White.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(letter, color = content, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Text(text, color = content, fontSize = 15.sp, modifier = Modifier.weight(1f))
        when (state) {
            OptionState.Correct -> Icon(Icons.Filled.CheckCircle, contentDescription = "Correct", tint = Color.White)
            OptionState.Wrong -> Icon(Icons.Filled.Cancel, contentDescription = "Wrong", tint = Color.White)
            else -> Unit
        }
    }
}
