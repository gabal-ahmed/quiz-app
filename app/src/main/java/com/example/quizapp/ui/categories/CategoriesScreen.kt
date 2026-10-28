package com.example.quizapp.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quizapp.data.Category
import com.example.quizapp.data.QuestionBank
import com.example.quizapp.ui.components.CategoryCard
import com.example.quizapp.ui.components.ScreenTopBar
import com.example.quizapp.ui.home.HomeViewModel

@Composable
fun CategoriesScreen(
    viewModel: HomeViewModel,
    onCategoryClick: (Category) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Categories")
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Category.entries.forEach { category ->
                val best = state.bestByCategory[category]
                CategoryCard(
                    category = category,
                    subtitle = "${QuestionBank.countFor(category)} questions • ${category.difficulty} difficulty\n${category.description}",
                    footer = best?.let { "Best score: ${it.correctAnswers}/${it.totalQuestions}" },
                    onClick = { onCategoryClick(category) },
                )
            }
        }
    }
}
