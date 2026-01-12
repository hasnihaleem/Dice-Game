package com.example.dicegame

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dicegame.viewmodel.GameViewModel  // Correct import path

@Composable
fun DiceGameAppScreen() {
    val viewModel = viewModel<GameViewModel>()
    DiceGameNavigation(viewModel)
}