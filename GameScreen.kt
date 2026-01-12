package com.example.dicegame.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dicegame.R
import com.example.dicegame.model.Dice
import com.example.dicegame.model.GameState
import com.example.dicegame.model.GameStatus
import com.example.dicegame.viewmodel.GameViewModel

@Composable
fun GameScreen(
    onNavigateToStart: () -> Unit,
    viewModel: GameViewModel
) {
    val gameState by viewModel.gameState.collectAsState()

    var showTargetDialog by rememberSaveable { mutableStateOf(false) }
    var targetScoreInput by rememberSaveable { mutableStateOf(gameState.targetScore.toString()) }

    val showGameResultDialog = gameState.gameStatus == GameStatus.HUMAN_WON || gameState.gameStatus == GameStatus.COMPUTER_WON

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("H:${gameState.humanWins}/C:${gameState.computerWins}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Target: ${gameState.targetScore}", modifier = Modifier.clickable { showTargetDialog = true }, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("H:${gameState.humanScore}/C:${gameState.computerScore}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        GameInfoDisplay(gameState)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Computer", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        DiceRow(gameState.computerDice, {}, false)
        Text("Current score: ${gameState.computerCurrentScore}")

        Spacer(modifier = Modifier.height(24.dp))

        Text("You", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        DiceRow(gameState.humanDice, { viewModel.toggleDiceSelection(it) }, gameState.rollCount in 1..2 && gameState.gameStatus == GameStatus.IN_PROGRESS)
        Text("Current score: ${gameState.humanCurrentScore}")

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = when (gameState.gameStatus) {
                GameStatus.FINAL_ROLL_TIEBREAKER -> "Tiebreaker roll!"
                else -> "Roll ${gameState.rollCount}/3"
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (gameState.gameStatus == GameStatus.FINAL_ROLL_TIEBREAKER) {
                        viewModel.handleTiebreaker()
                    } else {
                        viewModel.rollDice()
                    }
                },
                enabled = (gameState.rollCount < 3 && gameState.gameStatus == GameStatus.IN_PROGRESS)
                        || gameState.gameStatus == GameStatus.FINAL_ROLL_TIEBREAKER
            ) {
                Text(if (gameState.gameStatus == GameStatus.FINAL_ROLL_TIEBREAKER) "Tiebreaker Roll" else "Throw")
            }

            Button(
                onClick = { viewModel.scoreRoll() },
                enabled = gameState.rollCount > 0 && gameState.rollCount <= 3 && gameState.gameStatus == GameStatus.IN_PROGRESS
            ) {
                Text("Score")
            }
        }
    }

    if (showTargetDialog) {
        AlertDialog(
            onDismissRequest = { showTargetDialog = false },
            title = { Text("Set Target Score") },
            text = {
                OutlinedTextField(
                    value = targetScoreInput,
                    onValueChange = {
                        if (it.all { ch -> ch.isDigit() }) targetScoreInput = it
                    },
                    label = { Text("Target Score") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default.copy(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    )
                )
            },
            confirmButton = {
                Button(onClick = {
                    targetScoreInput.toIntOrNull()?.let {
                        if (it > 0) viewModel.setTargetScore(it)
                    }
                    showTargetDialog = false
                }) { Text("Set") }
            },
            dismissButton = {
                Button(onClick = { showTargetDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showGameResultDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = if (gameState.gameStatus == GameStatus.HUMAN_WON) "You win!" else "You lose",
                    color = if (gameState.gameStatus == GameStatus.HUMAN_WON) Color.Green else Color.Red
                )
            },
            text = {
                Text("Final score: You ${gameState.humanScore} - Computer ${gameState.computerScore}", textAlign = TextAlign.Center)
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.resetGame()
                    onNavigateToStart()
                }) {
                    Text("Back to Menu")
                }
            }
        )
    }
}

@Composable
fun DiceRow(
    diceList: List<Dice>,
    onDiceClick: (Int) -> Unit,
    clickable: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        diceList.forEachIndexed { index, dice ->
            DiceImage(
                dice = dice,
                onClick = { if (clickable) onDiceClick(index) },
                clickable = clickable
            )
        }
    }
}

@Composable
fun DiceImage(
    dice: Dice,
    onClick: () -> Unit,
    clickable: Boolean
) {
    val diceImageRes = when (dice.value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (dice.isSelected) Color.LightGray else Color.White)
            .border(1.dp, if (dice.isSelected) Color.Blue else Color.Gray, RoundedCornerShape(8.dp))
            .clickable(enabled = clickable) { onClick() }
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = diceImageRes),
            contentDescription = "Dice showing ${dice.value}",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun GameInfoDisplay(gameState: GameState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Game Status", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (gameState.gameStatus) {
                    GameStatus.IN_PROGRESS -> "Game in progress"
                    GameStatus.HUMAN_WON -> "You won!"
                    GameStatus.COMPUTER_WON -> "Computer won"
                    GameStatus.FINAL_ROLL_TIEBREAKER -> "Tiebreaker roll"
                },
                fontSize = 16.sp,
                color = when (gameState.gameStatus) {
                    GameStatus.HUMAN_WON -> Color.Green
                    GameStatus.COMPUTER_WON -> Color.Red
                    else -> Color.Black
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your Score", fontWeight = FontWeight.Bold)
                    Text("${gameState.humanScore}")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Computer Score", fontWeight = FontWeight.Bold)
                    Text("${gameState.computerScore}")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Target", fontWeight = FontWeight.Bold)
                    Text("${gameState.targetScore}")
                }
            }

            if (gameState.rollCount > 0 && gameState.gameStatus == GameStatus.IN_PROGRESS) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Roll ${gameState.rollCount}/3", fontSize = 16.sp)
                Text(
                    text = if (gameState.rollCount < 3) "Tap dice to select for keeping" else "Maximum rolls reached",
                    fontSize = 14.sp,
                    color = if (gameState.rollCount < 3) Color.Blue else Color.Red
                )
            }
        }
    }
}
