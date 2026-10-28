package com.example.quizapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.Category
import com.example.quizapp.data.QuizAttempt
import com.example.quizapp.data.ResultRepository
import com.example.quizapp.data.SettingsDataStore
import com.example.quizapp.data.bestAttempt
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val userName: String = "",
    val bestOverall: QuizAttempt? = null,
    val bestByCategory: Map<Category, QuizAttempt> = emptyMap(),
)

/** Shared by Home and Categories: user greeting plus best score overall and per category. */
class HomeViewModel(settings: SettingsDataStore, results: ResultRepository) : ViewModel() {

    val state: StateFlow<HomeUiState> = combine(settings.session, results.observeAll()) { session, attempts ->
        HomeUiState(
            userName = session?.name.orEmpty(),
            bestOverall = attempts.bestAttempt(),
            bestByCategory = Category.entries.mapNotNull { category ->
                attempts.filter { it.category == category.name }.bestAttempt()?.let { category to it }
            }.toMap(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())
}
