package entity
import kotlin.test.*
/**
 * test for card value strings and their order
 */
class CardValueTest {
    @Test
    fun testValueStrings() {
        assertEquals("2", CardValue.TWO.toString())
        assertEquals("10", CardValue.TEN.toString())
        assertEquals("J", CardValue.JACK.toString())
        assertEquals("A", CardValue.ACE.toString())
    }
    @Test
    fun testValueOrder() {
        assertTrue(CardValue.TWO < CardValue.ACE)
        assertTrue(CardValue.KING > CardValue.QUEEN)
        assertTrue(CardValue.SEVEN < CardValue.EIGHT)
    }
}