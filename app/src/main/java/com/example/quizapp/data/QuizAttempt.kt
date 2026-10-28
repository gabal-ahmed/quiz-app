package com.example.quizapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    /** [Category.name] of the quiz category. */
    val category: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val scorePercentage: Int,
    val durationSeconds: Long,
    val completedAt: Long,
)

fun QuizAttempt.categoryOrNull(): Category? = Category.fromName(category)

/** Highest percentage wins; on a tie the most recent attempt wins. */
fun List<QuizAttempt>.bestAttempt(): QuizAttempt? =
    maxWithOrNull(compareBy<QuizAttempt> { it.scorePercentage }.thenBy { it.completedAt })
