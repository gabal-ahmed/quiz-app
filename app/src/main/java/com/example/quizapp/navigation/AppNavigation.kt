package com.example.quizapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizapp.AppContainer
import com.example.quizapp.data.Category
import com.example.quizapp.ui.auth.AuthViewModel
import com.example.quizapp.ui.auth.LoginScreen
import com.example.quizapp.ui.auth.RegisterScreen
import com.example.quizapp.ui.categories.CategoriesScreen
import com.example.quizapp.ui.home.HomeScreen
import com.example.quizapp.ui.home.HomeViewModel
import com.example.quizapp.ui.keepgoing.KeepGoingScreen
import com.example.quizapp.ui.quiz.QuizScreen
import com.example.quizapp.ui.quiz.QuizViewModel
import com.example.quizapp.ui.results.HistoryViewModel
import com.example.quizapp.ui.results.ResultScreen
import com.example.quizapp.ui.results.ResultViewModel
import com.example.quizapp.ui.results.ResultsHistoryScreen
import com.example.quizapp.ui.settings.AboutScreen
import com.example.quizapp.ui.settings.SettingsScreen
import com.example.quizapp.ui.settings.SettingsViewModel
import com.example.quizapp.ui.splash.SplashScreen
import com.example.quizapp.ui.theme.AppTheme
import com.example.quizapp.ui.theme.Brand
import kotlinx.coroutines.flow.first

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val HISTORY = "history"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val KEEP_GOING = "keep_going"
    const val QUIZ = "quiz/{${QuizViewModel.ARG_CATEGORY}}"
    const val RESULT = "result/{${ResultViewModel.ARG_ATTEMPT_ID}}"

    fun quiz(category: Category) = "quiz/${category.name}"
    fun result(attemptId: Long) = "result/$attemptId"
}

/** Results below this percentage get the optional "Keep Going" screen before returning home. */
private const val KEEP_GOING_THRESHOLD = 70

private data class BottomTab(val route: String, val label: String, val icon: ImageVector)

private val BottomTabs = listOf(
    BottomTab(Routes.HOME, "Home", Icons.Outlined.Home),
    BottomTab(Routes.CATEGORIES, "Categories", Icons.Outlined.GridView),
    BottomTab(Routes.HISTORY, "Results", Icons.Outlined.BarChart),
    BottomTab(Routes.SETTINGS, "Settings", Icons.Outlined.Settings),
)

@Composable
fun AppNavigation(container: AppContainer) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val factory = remember(container) {
        viewModelFactory {
            initializer { AuthViewModel(container.authRepository) }
            initializer { HomeViewModel(container.settings, container.resultRepository) }
            initializer { QuizViewModel(createSavedStateHandle(), container.resultRepository) }
            initializer { ResultViewModel(createSavedStateHandle(), container.resultRepository) }
            initializer { HistoryViewModel(container.resultRepository) }
            initializer { SettingsViewModel(container.settings, container.authRepository, container.resultRepository) }
        }
    }

    // Leaves the app's back stack holding only `route` (used after login/logout).
    fun resetTo(route: String) = navController.navigate(route) {
        popUpTo(navController.graph.id) { inclusive = true }
    }

    fun backToHome() {
        if (!navController.popBackStack(Routes.HOME, inclusive = false)) resetTo(Routes.HOME)
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = AppTheme.colors.textPrimary,
        bottomBar = {
            if (BottomTabs.any { it.route == currentRoute }) {
                AppBottomBar(currentRoute, onTabSelected = navController::navigateToTab)
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    isLoggedIn = { container.authRepository.session.first() != null },
                    onFinished = { loggedIn -> resetTo(if (loggedIn) Routes.HOME else Routes.LOGIN) },
                )
            }
            composable(Routes.LOGIN) {
                LoginScreen(
                    viewModel = viewModel(factory = factory),
                    onLoggedIn = { resetTo(Routes.HOME) },
                    onRegisterClick = { navController.navigate(Routes.REGISTER) },
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    viewModel = viewModel(factory = factory),
                    onRegistered = { resetTo(Routes.HOME) },
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Routes.HOME) {
                HomeScreen(
                    viewModel = viewModel(factory = factory),
                    onCategoryClick = { navController.navigate(Routes.quiz(it)) },
                    onBestScoreClick = { navController.navigateToTab(Routes.HISTORY) },
                    onSettingsClick = { navController.navigateToTab(Routes.SETTINGS) },
                )
            }
            composable(Routes.CATEGORIES) {
                CategoriesScreen(
                    viewModel = viewModel(factory = factory),
                    onCategoryClick = { navController.navigate(Routes.quiz(it)) },
                )
            }
            composable(
                Routes.QUIZ,
                arguments = listOf(navArgument(QuizViewModel.ARG_CATEGORY) { type = NavType.StringType }),
            ) {
                QuizScreen(
                    viewModel = viewModel(factory = factory),
                    onQuizFinished = { attemptId ->
                        navController.navigate(Routes.result(attemptId)) {
                            popUpTo(Routes.QUIZ) { inclusive = true }
                        }
                    },
                    onExit = { navController.popBackStack() },
                )
            }
            composable(
                Routes.RESULT,
                arguments = listOf(navArgument(ResultViewModel.ARG_ATTEMPT_ID) { type = NavType.LongType }),
            ) {
                ResultScreen(
                    viewModel = viewModel(factory = factory),
                    onTryAgain = { category ->
                        navController.navigate(Routes.quiz(category)) {
                            popUpTo(Routes.RESULT) { inclusive = true }
                        }
                    },
                    onBackToHome = { attempt ->
                        if (attempt.scorePercentage < KEEP_GOING_THRESHOLD) {
                            navController.navigate(Routes.KEEP_GOING) {
                                popUpTo(Routes.RESULT) { inclusive = true }
                            }
                        } else {
                            backToHome()
                        }
                    },
                )
            }
            composable(Routes.HISTORY) {
                ResultsHistoryScreen(
                    viewModel = viewModel(factory = factory),
                    onAttemptClick = { navController.navigate(Routes.result(it.id)) },
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    viewModel = viewModel(factory = factory),
                    onAboutClick = { navController.navigate(Routes.ABOUT) },
                    onLoggedOut = { resetTo(Routes.LOGIN) },
                )
            }
            composable(Routes.ABOUT) {
                AboutScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.KEEP_GOING) {
                KeepGoingScreen(onBackToHome = ::backToHome)
            }
        }
    }
}

// Home is the root of the back stack once the user is logged in (Splash/Login are removed).
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun AppBottomBar(currentRoute: String?, onTabSelected: (String) -> Unit) {
    val c = AppTheme.colors
    NavigationBar(containerColor = c.surface, contentColor = c.textSecondary) {
        BottomTabs.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab.route,
                onClick = { onTabSelected(tab.route) },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppTheme.colors.accent,
                    selectedTextColor = AppTheme.colors.accent,
                    indicatorColor = Brand.Purple.copy(alpha = 0.18f),
                    unselectedIconColor = c.textSecondary,
                    unselectedTextColor = c.textSecondary,
                ),
            )
        }
    }
}
