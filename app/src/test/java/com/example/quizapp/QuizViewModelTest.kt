package com.example.quizapp

import androidx.lifecycle.SavedStateHandle
import com.example.quizapp.data.Category
import com.example.quizapp.data.QuizAttempt
import com.example.quizapp.data.QuizAttemptDao
import com.example.quizapp.data.ResultRepository
import com.example.quizapp.ui.quiz.QuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    private class FakeDao : QuizAttemptDao {
        val attempts = MutableStateFlow<List<QuizAttempt>>(emptyList())
        override suspend fun insert(attempt: QuizAttempt): Long {
            val id = attempts.value.size + 1L
            attempts.value = attempts.value + attempt.copy(id = id)
            return id
        }
        override fun observeAll(): Flow<List<QuizAttempt>> = attempts
        override fun observeById(id: Long): Flow<QuizAttempt?> = attempts.map { list -> list.firstOrNull { it.id == id } }
        override suspend fun clearAll() { attempts.value = emptyList() }
    }

    private val dao = FakeDao()
    private var now = 0L

    @Before
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    private fun newViewModel() = QuizViewModel(
        SavedStateHandle(mapOf(QuizViewModel.ARG_CATEGORY to Category.NETWORKS.name)),
        ResultRepository(dao),
        clock = { now },
    )

    @Test
    fun nextIsIgnoredUntilAnAnswerIsSelected() {
        val vm = newViewModel()
        vm.next()
        assertEquals(0, vm.state.value.currentIndex)
    }

    @Test
    fun anAnswerIsCountedOnlyOnce() {
        val vm = newViewModel()
        val correct = vm.state.value.currentQuestion!!.correctAnswerIndex
        vm.selectAnswer(correct)
        vm.selectAnswer(correct)
        vm.selectAnswer((correct + 1) % 4)
        assertEquals(1, vm.state.value.correctCount)
        assertEquals(correct, vm.state.value.selectedIndex)
        assertFalse(vm.state.value.showWrongAnswer)
    }

    @Test
    fun wrongAnswerShowsFeedback() {
        val vm = newViewModel()
        val wrong = (vm.state.value.currentQuestion!!.correctAnswerIndex + 1) % 4
        vm.selectAnswer(wrong)
        assertTrue(vm.state.value.showWrongAnswer)
        assertEquals(0, vm.state.value.correctCount)
    }

    @Test
    fun finishingSavesExactlyOneAttemptWithScoreAndDuration() {
        val vm = newViewModel()
        repeat(10) { i ->
            val q = vm.state.value.currentQuestion!!
            // Answer the first 8 correctly and the last 2 wrong.
            vm.selectAnswer(if (i < 8) q.correctAnswerIndex else (q.correctAnswerIndex + 1) % 4)
            if (i == 9) now = 272_000L
            vm.next()
        }
        vm.next() // extra taps after finishing must not save again

        assertNotNull(vm.state.value.completedAttemptId)
        val saved = dao.attempts.value.single()
        assertEquals(Category.NETWORKS.name, saved.category)
        assertEquals(10, saved.totalQuestions)
        assertEquals(8, saved.correctAnswers)
        assertEquals(80, saved.scorePercentage)
        assertEquals(272L, saved.durationSeconds)
    }
}
