package com.example.dicegame.model

data class Dice(
    val value: Int,
    val isSelected: Boolean = false
)

data class GameState(
    val humanDice: List<Dice> = List(5) { Dice(1) },
    val computerDice: List<Dice> = List(5) { Dice(1) },
    val humanScore: Int = 0,
    val computerScore: Int = 0,
    val humanCurrentScore: Int = 0,
    val computerCurrentScore: Int = 0,
    val rollCount: Int = 0,
    val targetScore: Int = 101,
    val humanWins: Int = 0,
    val computerWins: Int = 0,
    val gameStatus: GameStatus = GameStatus.IN_PROGRESS,
    val humanRollCount: Int = 0,
    val computerRollCount: Int = 0
)

enum class GameStatus {
    IN_PROGRESS,
    HUMAN_WON,
    COMPUTER_WON,
    FINAL_ROLL_TIEBREAKER
}
