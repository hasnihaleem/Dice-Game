package com.example.dicegame

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dicegame.ui.AboutScreen
import com.example.dicegame.ui.GameScreen
import com.example.dicegame.ui.StartScreen
import com.example.dicegame.viewmodel.GameViewModel

@Composable
fun DiceGameNavigation(viewModel: GameViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(
                onNewGameClick = {
                    viewModel.resetGame()
                    navController.navigate("game")
                },
                onAboutClick = {
                    navController.navigate("about")
                }
            )
        }

        composable("game") {
            GameScreen(
                viewModel = viewModel,
                onNavigateToStart = {
                    navController.navigate("start") {
                        popUpTo("start") { inclusive = true }
                    }
                }
            )
        }

        composable("about") {
            AboutScreen(
                onBack = { navController.navigateUp() }
            )
        }
    }
}
