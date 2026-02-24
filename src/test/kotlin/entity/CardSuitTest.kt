package entity
import kotlin.test.*
/**
 * test for card suit strings
 */
class CardSuitTest {
    /**
     * test if suit symbols match
     */
    @Test
    fun testSuitStrings() {
        assertEquals("♣", CardSuit.CLUBS.toString())
        assertEquals("♠", CardSuit.SPADES.toString())
        assertEquals("♥", CardSuit.HEARTS.toString())
        assertEquals("♦", CardSuit.DIAMONDS.toString())
    }
}