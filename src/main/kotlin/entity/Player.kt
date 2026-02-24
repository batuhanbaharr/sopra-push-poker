package entity
/**
 * represent a player in the game
 */
data class Player(
    val name: String,
    var actionsLeft: Int = 2,
    val hiddenCards: MutableList<Card> = mutableListOf(),
    val openCards: MutableList<Card> = mutableListOf(),
    var score: ScoreTable = ScoreTable.HIGHCARD
)