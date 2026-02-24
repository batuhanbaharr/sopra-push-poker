package entity
import tools.aqua.bgw.util.Stack
/**
 * hold the current state of schiebe-poker game
 */
data class Game(
    val totalRounds: Int,
    var currentRound: Int  = totalRounds,
    val players: MutableList<Player> = mutableListOf(),
    val drawStack: Stack<Card> = Stack(),
    val discardStack: Stack<Card> = Stack(),
    val centerCards: MutableList<Card> = mutableListOf(),
    var currentPlayerIndex: Int = 0,
    val log: MutableList<String> = mutableListOf()
)