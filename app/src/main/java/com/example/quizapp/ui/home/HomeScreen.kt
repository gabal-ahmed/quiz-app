package com.example.quizapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quizapp.data.Category
import com.example.quizapp.ui.components.AppTitle
import com.example.quizapp.ui.components.BestScoreCard
import com.example.quizapp.ui.components.CategoryCard
import com.example.quizapp.ui.components.UserAvatar
import com.example.quizapp.ui.theme.AppTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCategoryClick: (Category) -> Unit,
    onBestScoreClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val c = AppTheme.colors
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            UserAvatar(state.userName)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Welcome Back,", color = c.textSecondary, fontSize = 13.sp)
                Text(state.userName, color = c.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = c.textPrimary)
            }
        }

        Spacer(Modifier.height(20.dp))
        AppTitle(Modifier.fillMaxWidth(), fontSize = 34.sp)
        Text(
            "Choose a category to start your quiz",
            color = c.textSecondary,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(Modifier.height(20.dp))
        Category.entries.forEach { category ->
            CategoryCard(
                category = category,
                subtitle = category.description,
                onClick = { onCategoryClick(category) },
            )
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(4.dp))
        BestScoreCard(best = state.bestOverall, onClick = onBestScoreClick)
    }
}
