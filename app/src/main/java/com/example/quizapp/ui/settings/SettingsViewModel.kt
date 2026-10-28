package com.example.quizapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.DemoAuthRepository
import com.example.quizapp.data.ResultRepository
import com.example.quizapp.data.SettingsDataStore
import com.example.quizapp.data.UserSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settings: SettingsDataStore,
    private val auth: DemoAuthRepository,
    private val results: ResultRepository,
) : ViewModel() {

    val session: StateFlow<UserSession?> = settings.session
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val darkMode: StateFlow<Boolean> = settings.darkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { settings.setDarkMode(enabled) }
    }

    fun clearResults(onCleared: () -> Unit) {
        viewModelScope.launch {
            results.clearAll()
            onCleared()
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            auth.logout()
            onLoggedOut()
        }
    }
}
