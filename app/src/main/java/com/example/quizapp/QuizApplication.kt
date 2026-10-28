package com.example.quizapp

import android.app.Application
import android.content.Context
import com.example.quizapp.data.AppDatabase
import com.example.quizapp.data.DemoAuthRepository
import com.example.quizapp.data.ResultRepository
import com.example.quizapp.data.SettingsDataStore

class QuizApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

/** Simple manual dependency container shared by the whole app. */
class AppContainer(context: Context) {
    val settings = SettingsDataStore(context)
    val resultRepository = ResultRepository(AppDatabase.getInstance(context).quizAttemptDao())
    val authRepository = DemoAuthRepository(settings)
}
