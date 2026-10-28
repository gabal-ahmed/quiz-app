package com.example.quizapp.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizAttemptDao {
    @Insert
    suspend fun insert(attempt: QuizAttempt): Long

    @Query("SELECT * FROM quiz_attempts ORDER BY completedAt DESC")
    fun observeAll(): Flow<List<QuizAttempt>>

    @Query("SELECT * FROM quiz_attempts WHERE id = :id")
    fun observeById(id: Long): Flow<QuizAttempt?>

    @Query("DELETE FROM quiz_attempts")
    suspend fun clearAll()
}

@Database(entities = [QuizAttempt::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizAttemptDao(): QuizAttemptDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "quiz_app.db")
                    .build()
                    .also { instance = it }
            }
    }
}
