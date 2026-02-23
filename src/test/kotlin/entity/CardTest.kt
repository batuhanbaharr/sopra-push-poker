package entity

import kotlin.test.*

class CardTest {
    private val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val otherQueenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val jackOfDiamonds = Card(CardSuit.DIAMONDS, CardValue.JACK)
    private val heartsChar = "♥"
    private val diamondsChar = "♦"
    private val spadesChar = "♠"
    private val clubsChar = "♣"
    @Test
    fun testToString() {
        assertEquals(spadesChar + "A", aceOfSpades.toString())
        assertEquals(clubsChar + "J", jackOfClubs.toString())
        assertEquals(heartsChar + "Q", queenOfHearts.toString())
        assertEquals(diamondsChar + "J", jackOfDiamonds.toString())
    }
    @Test
    fun testToStringLength() {
        CardSuit.entries.forEach { suit ->
            CardValue.entries.forEach { value ->
                if (value == CardValue.TEN)
                    assertEquals(3, Card(suit, value).toString().length)
                else
                    assertEquals(2, Card(suit, value).toString().length)
            }
        }
    }
    @Test
    fun testEquals() {
        assertEquals(queenOfHearts, otherQueenOfHearts)
        assertNotSame(queenOfHearts, otherQueenOfHearts)
    }
}