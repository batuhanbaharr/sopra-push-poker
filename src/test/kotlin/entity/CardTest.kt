package entity
import kotlin.test.*
/**
 * test cases for card combinations
 */
class CardTest {
    @Test
    fun testToString() {
        val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
        val tenOfHearts = Card(CardSuit.HEARTS, CardValue.TEN)
        assertEquals("♠A", aceOfSpades.toString())
        assertEquals("♥10", tenOfHearts.toString())
    }
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
    @Test
    fun testEquals() {
        val card1 = Card(CardSuit.HEARTS, CardValue.QUEEN)
        val card2 = Card(CardSuit.HEARTS, CardValue.QUEEN)
        assertEquals(card1, card2)
        assertNotSame(card1, card2)
    }
}