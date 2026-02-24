package entity
import kotlin.test.*
/**
 * test cases for card combinations
 */
class CardTest {
    /**
     * test string format of cards
     */
    @Test
    fun testToString() {
        val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
        val tenOfHearts = Card(CardSuit.HEARTS, CardValue.TEN)
        assertEquals("♠A", aceOfSpades.toString())
        assertEquals("♥10", tenOfHearts.toString())
    }
    /**
     * test string length of cards
     */
    @Test
    fun testToStringLength() {
        CardSuit.entries.forEach { suit ->
            CardValue.entries.forEach { value ->
                val card = Card(suit, value)
                if (value == CardValue.TEN) {
                    assertEquals(3, card.toString().length)
                } else {
                    assertEquals(2, card.toString().length)
                }
            }
        }
    }
    /**
     * test equality of identical and different cards
     */
    @Test
    fun testEquals() {
        val card1 = Card(CardSuit.HEARTS, CardValue.QUEEN)
        val card2 = Card(CardSuit.HEARTS, CardValue.QUEEN)
        val differentCard = Card(CardSuit.SPADES, CardValue.ACE)
        assertEquals(card1, card2)
        assertNotEquals(card1, differentCard)
    }
}