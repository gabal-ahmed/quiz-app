package com.example.quizapp.data

enum class Category(
    val title: String,
    val shortTitle: String,
    val description: String,
    val difficulty: String,
) {
    PROGRAMMING(
        title = "Programming",
        shortTitle = "Programming",
        description = "Test your knowledge in programming concepts.",
        difficulty = "Medium",
    ),
    NETWORKS(
        title = "Computer Networks",
        shortTitle = "Networks",
        description = "Explore the world of networking.",
        difficulty = "Medium",
    ),
    CS_BASICS(
        title = "Computer Science Basics",
        shortTitle = "CS Basics",
        description = "General knowledge about computer science.",
        difficulty = "Easy",
    );

    companion object {
        fun fromName(name: String?): Category? = entries.firstOrNull { it.name == name }
    }
}

data class Question(
    val id: Int,
    val category: Category,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    /** Optional code snippet shown under the question text. */
    val code: String? = null,
) {
    init {
        require(options.size == 4) { "Question $id must have exactly 4 options" }
        require(correctAnswerIndex in options.indices) { "Question $id has an invalid answer index" }
    }

    val correctAnswer: String get() = options[correctAnswerIndex]

    /** Returns a copy with the options in random order, keeping the correct answer index in sync. */
    fun withShuffledOptions(): Question {
        val order = options.indices.shuffled()
        return copy(
            options = order.map { options[it] },
            correctAnswerIndex = order.indexOf(correctAnswerIndex),
        )
    }
}
