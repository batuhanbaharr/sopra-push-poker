package entity
/**
 * represent a single playing card
 */
data class Card(
    val suit: CardSuit,
    val value: CardValue
) {
    /**
     * return the card
     */
    override fun toString() = "$suit$value"
}