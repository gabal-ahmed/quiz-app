package com.example.quizapp.ui.results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.Category
import com.example.quizapp.data.QuizAttempt
import com.example.quizapp.data.ResultRepository
import com.example.quizapp.data.bestAttempt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ResultViewModel(savedStateHandle: SavedStateHandle, results: ResultRepository) : ViewModel() {

    private val attemptId: Long = checkNotNull(savedStateHandle[ARG_ATTEMPT_ID])

    val attempt: StateFlow<QuizAttempt?> = results.observeById(attemptId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    companion object {
        const val ARG_ATTEMPT_ID = "attemptId"
    }
}

data class HistoryUiState(
    val filter: Category? = null,
    val attempts: List<QuizAttempt> = emptyList(),
    val best: QuizAttempt? = null,
    val isLoading: Boolean = true,
)

class HistoryViewModel(results: ResultRepository) : ViewModel() {

    private val filter = MutableStateFlow<Category?>(null)

    val state: StateFlow<HistoryUiState> = combine(results.observeAll(), filter) { all, selected ->
        val visible = if (selected == null) all else all.filter { it.category == selected.name }
        HistoryUiState(filter = selected, attempts = visible, best = visible.bestAttempt(), isLoading = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryUiState())

    fun setFilter(category: Category?) {
        filter.value = category
    }
}
