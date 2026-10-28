package com.example.quizapp.data

import kotlinx.coroutines.flow.Flow
import kotlin.math.roundToInt

class ResultRepository(private val dao: QuizAttemptDao) {

    fun observeAll(): Flow<List<QuizAttempt>> = dao.observeAll()

    fun observeById(id: Long): Flow<QuizAttempt?> = dao.observeById(id)

    suspend fun saveAttempt(
        category: Category,
        totalQuestions: Int,
        correctAnswers: Int,
        durationSeconds: Long,
    ): Long = dao.insert(
        QuizAttempt(
            category = category.name,
            totalQuestions = totalQuestions,
            correctAnswers = correctAnswers,
            scorePercentage = scorePercentage(correctAnswers, totalQuestions),
            durationSeconds = durationSeconds,
            completedAt = System.currentTimeMillis(),
        )
    )

    suspend fun clearAll() = dao.clearAll()

    companion object {
        /** Correct answers ÷ total questions × 100. */
        fun scorePercentage(correct: Int, total: Int): Int =
            if (total == 0) 0 else (correct * 100f / total).roundToInt()
    }
}
