package com.example.quizapp

import com.example.quizapp.data.Category
import com.example.quizapp.data.QuestionBank
import com.example.quizapp.data.QuizAttempt
import com.example.quizapp.data.ResultRepository
import com.example.quizapp.data.bestAttempt
import org.junit.Assert.assertEquals
import org.junit.Test

class QuizRulesTest {

    @Test
    fun eachCategoryHasTenQuestionsWithUniqueIds() {
        Category.entries.forEach { assertEquals(10, QuestionBank.countFor(it)) }
        assertEquals(30, QuestionBank.questions.map { it.id }.toSet().size)
    }

    @Test
    fun shufflingOptionsKeepsTheCorrectAnswer() {
        QuestionBank.questions.forEach { question ->
            repeat(20) {
                assertEquals(question.correctAnswer, question.withShuffledOptions().correctAnswer)
            }
        }
    }

    @Test
    fun scoreIsCorrectAnswersOverTotalTimesHundred() {
        assertEquals(80, ResultRepository.scorePercentage(8, 10))
        assertEquals(0, ResultRepository.scorePercentage(0, 10))
        assertEquals(100, ResultRepository.scorePercentage(10, 10))
        assertEquals(0, ResultRepository.scorePercentage(0, 0))
    }

    @Test
    fun bestAttemptPrefersHigherScoreThenMostRecent() {
        fun attempt(id: Long, score: Int, at: Long) = QuizAttempt(id, "PROGRAMMING", 10, score / 10, score, 60, at)
        val attempts = listOf(attempt(1, 80, 100), attempt(2, 90, 50), attempt(3, 90, 200), attempt(4, 40, 300))
        assertEquals(3L, attempts.bestAttempt()?.id)
    }
}
