package entity
/**
 * represents a player in the game
 * @param name the name of the player
 * @property hiddenCards the cards only this player can see
 * @property openCards the cards everyone can see
 * @property actionsLeft the number of actions the player can still do
 * @property score the value of the player's cards
 */
data class Player(val name: String) {
    val hiddenCards: MutableList<Card> = mutableListOf()
    val openCards: MutableList<Card> = mutableListOf()
    var actionsLeft: Int = 2
    var score: ScoreTable = ScoreTable.HIGHCARD
}