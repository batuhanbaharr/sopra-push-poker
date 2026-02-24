package entity
import tools.aqua.bgw.util.Stack
/**
 * holds the current state of game
 * @param players the players in the game
 * @param totalRounds the total number of rounds to play
 * @property currentRound the current round (starts at 1)
 * @property drawStack the stack of cards to draw from
 * @property discardStack the stack for played cards
 * @property centerCards the open cards in the middle of the table
 * @property currentPlayerIndex the index of the active player
 * @property log text log of game events
 */
data class Game(
    val players: MutableList<Player>,
    val totalRounds: Int
) {
    var currentRound: Int = 1
    val drawStack: Stack<Card> = Stack()
    val discardStack: Stack<Card> = Stack()
    val centerCards: MutableList<Card> = mutableListOf()
    var currentPlayerIndex: Int = 0
    val log: MutableList<String> = mutableListOf()

}