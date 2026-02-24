package entity
import kotlin.test.*
/**
 * test cases for poker hand rankings
 */
class ScoreTableTest {
    @Test
    fun testScoreTableOrder() {
        assertTrue(ScoreTable.HIGHCARD.ordinal < ScoreTable.PAIR.ordinal)
        assertTrue(ScoreTable.SET.ordinal > ScoreTable.TWOPAIR.ordinal)
        assertTrue(ScoreTable.FLUSH.ordinal > ScoreTable.STRAIGHT.ordinal)
        assertTrue(ScoreTable.ROYALFLUSH.ordinal > ScoreTable.STRAIGHTFLUSH.ordinal)
    }
    @Test
    fun testOrdinals() {
        assertEquals(0, ScoreTable.HIGHCARD.ordinal)
        assertEquals(9, ScoreTable.ROYALFLUSH.ordinal)
    }
}