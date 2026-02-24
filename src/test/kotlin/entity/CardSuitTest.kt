package entity
import kotlin.test.*
/**
 * test for card suit strings
 */
class CardSuitTest {
    @Test
    fun testSuitStrings() {
        assertEquals("♣", CardSuit.CLUBS.toString())
        assertEquals("♠", CardSuit.SPADES.toString())
        assertEquals("♥", CardSuit.HEARTS.toString())
        assertEquals("♦", CardSuit.DIAMONDS.toString())
    }
}