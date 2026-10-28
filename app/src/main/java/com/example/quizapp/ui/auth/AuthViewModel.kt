package com.example.quizapp.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.DemoAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
    val isAuthenticated: Boolean = false,
)

class AuthViewModel(private val auth: DemoAuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        val errors = AuthUiState(
            emailError = validateEmail(email),
            passwordError = validatePassword(password),
        )
        _state.value = errors
        if (errors.emailError != null || errors.passwordError != null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            auth.login(email).fold(
                onSuccess = { _state.value = AuthUiState(isAuthenticated = true) },
                onFailure = { e -> _state.value = AuthUiState(generalError = e.message) },
            )
        }
    }

    fun register(fullName: String, email: String, password: String, confirmPassword: String) {
        val errors = AuthUiState(
            fullNameError = if (fullName.isBlank()) "Full name is required" else null,
            emailError = validateEmail(email),
            passwordError = validatePassword(password),
            confirmPasswordError = when {
                confirmPassword.isEmpty() -> "Please confirm your password"
                confirmPassword != password -> "Passwords do not match"
                else -> null
            },
        )
        _state.value = errors
        if (listOf(errors.fullNameError, errors.emailError, errors.passwordError, errors.confirmPasswordError).any { it != null }) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            auth.register(fullName, email).fold(
                onSuccess = { _state.value = AuthUiState(isAuthenticated = true) },
                onFailure = { e -> _state.value = AuthUiState(generalError = e.message) },
            )
        }
    }

    private fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Email is required"
        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Enter a valid email address"
        else -> null
    }

    private fun validatePassword(password: String): String? = when {
        password.isEmpty() -> "Password is required"
        password.length < MIN_PASSWORD_LENGTH -> "Password must be at least $MIN_PASSWORD_LENGTH characters"
        else -> null
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }
}
