package com.example.quizapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class UserSession(val name: String, val email: String)

/**
 * App preferences and the demo-mode profile. Only name and email are stored here;
 * passwords are never written to disk.
 */
class SettingsDataStore(context: Context) {

    private val store = context.applicationContext.dataStore

    val darkMode: Flow<Boolean> = store.data.map { it[DARK_MODE] ?: true }

    val session: Flow<UserSession?> = store.data.map { prefs ->
        val email = prefs[SESSION_EMAIL] ?: return@map null
        UserSession(name = prefs[SESSION_NAME].orEmpty(), email = email)
    }

    suspend fun setDarkMode(enabled: Boolean) {
        store.edit { it[DARK_MODE] = enabled }
    }

    suspend fun setSession(session: UserSession) {
        store.edit {
            it[SESSION_EMAIL] = session.email
            it[SESSION_NAME] = session.name
        }
    }

    suspend fun clearSession() {
        store.edit {
            it.remove(SESSION_EMAIL)
            it.remove(SESSION_NAME)
        }
    }

    /** Returns false if an account with this email already exists on the device. */
    suspend fun addAccount(name: String, email: String): Boolean {
        var added = false
        store.edit { prefs ->
            val accounts = prefs[ACCOUNTS].orEmpty()
            if (accounts.none { it.substringBefore(SEPARATOR) == email }) {
                prefs[ACCOUNTS] = accounts + "$email$SEPARATOR$name"
                added = true
            }
        }
        return added
    }

    suspend fun findAccountName(email: String): String? =
        store.data.first()[ACCOUNTS].orEmpty()
            .firstOrNull { it.substringBefore(SEPARATOR) == email }
            ?.substringAfter(SEPARATOR)

    private companion object {
        const val SEPARATOR = "\n"
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val SESSION_EMAIL = stringPreferencesKey("session_email")
        val SESSION_NAME = stringPreferencesKey("session_name")
        val ACCOUNTS = stringSetPreferencesKey("demo_accounts")
    }
}
