package com.example.quizapp.ui.quiz

import android.os.SystemClock
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.Category
import com.example.quizapp.data.QuestionBank
import com.example.quizapp.data.Question
import com.example.quizapp.data.ResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
    val category: Category,
    val questions: List<Question>,
    val currentIndex: Int = 0,
    val selectedIndex: Int? = null,
    val correctCount: Int = 0,
    val showWrongAnswer: Boolean = false,
    val isSaving: Boolean = false,
    val completedAttemptId: Long? = null,
) {
    val currentQuestion: Question? get() = questions.getOrNull(currentIndex)
    val isAnswered: Boolean get() = selectedIndex != null
    val isLastQuestion: Boolean get() = currentIndex == questions.lastIndex
    val progress: Float get() = if (questions.isEmpty()) 0f else (currentIndex + 1f) / questions.size
}

/**
 * Quiz rules from the spec: one answer per question, an answer is counted once,
 * Next only works after answering, and the attempt is saved exactly once at the end.
 */
class QuizViewModel(
    savedStateHandle: SavedStateHandle,
    private val results: ResultRepository,
    private val clock: () -> Long = SystemClock::elapsedRealtime,
) : ViewModel() {

    private val category = Category.fromName(savedStateHandle[ARG_CATEGORY]) ?: Category.PROGRAMMING
    private val startedAt = clock()

    private val _state = MutableStateFlow(
        QuizUiState(
            category = category,
            questions = QuestionBank.questionsFor(category).shuffled().map { it.withShuffledOptions() },
        )
    )
    val state: StateFlow<QuizUiState> = _state.asStateFlow()

    fun selectAnswer(index: Int) {
        val s = _state.value
        val question = s.currentQuestion ?: return
        if (s.isAnswered || s.isSaving) return
        val isCorrect = index == question.correctAnswerIndex
        _state.value = s.copy(
            selectedIndex = index,
            correctCount = s.correctCount + if (isCorrect) 1 else 0,
            showWrongAnswer = !isCorrect,
        )
    }

    fun next() {
        val s = _state.value
        if (!s.isAnswered || s.isSaving || s.completedAttemptId != null) return
        if (s.isLastQuestion) {
            finish()
        } else {
            _state.value = s.copy(currentIndex = s.currentIndex + 1, selectedIndex = null, showWrongAnswer = false)
        }
    }

    private fun finish() {
        _state.update { it.copy(isSaving = true, showWrongAnswer = false) }
        val s = _state.value
        val durationSeconds = (clock() - startedAt) / 1000
        viewModelScope.launch {
            val id = results.saveAttempt(category, s.questions.size, s.correctCount, durationSeconds)
            _state.update { it.copy(completedAttemptId = id) }
        }
    }

    companion object {
        const val ARG_CATEGORY = "category"
    }
}
