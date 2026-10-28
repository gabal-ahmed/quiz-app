package com.example.quizapp.data

import kotlinx.coroutines.flow.Flow

/**
 * Local demo authentication, as allowed by the spec when Firebase Authentication is not set up.
 * Accounts (name + email) live on this device only. Passwords are validated for format
 * but are neither stored nor verified.
 */
class DemoAuthRepository(private val settings: SettingsDataStore) {

    val session: Flow<UserSession?> = settings.session

    suspend fun login(email: String): Result<UserSession> {
        val key = email.trim().lowercase()
        val name = settings.findAccountName(key)
            ?: return Result.failure(AuthException("No account found for this email. Please register first."))
        val session = UserSession(name = name, email = key)
        settings.setSession(session)
        return Result.success(session)
    }

    suspend fun register(fullName: String, email: String): Result<UserSession> {
        val key = email.trim().lowercase()
        val name = fullName.trim()
        if (!settings.addAccount(name, key)) {
            return Result.failure(AuthException("An account with this email already exists. Please log in."))
        }
        val session = UserSession(name = name, email = key)
        settings.setSession(session)
        return Result.success(session)
    }

    suspend fun logout() = settings.clearSession()
}

class AuthException(message: String) : Exception(message)
