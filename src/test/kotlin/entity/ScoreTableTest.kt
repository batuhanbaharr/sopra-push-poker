package entity
import kotlin.test.*
/**
 * test cases for poker hand rankings
 */
class ScoreTableTest {
    /** check if order of hands is correct*/
    @Test
    fun testScoreTableOrder() {
        assertTrue(ScoreTable.HIGHCARD.ordinal < ScoreTable.PAIR.ordinal)
        assertTrue(ScoreTable.SET.ordinal > ScoreTable.TWOPAIR.ordinal)
        assertTrue(ScoreTable.FLUSH.ordinal > ScoreTable.STRAIGHT.ordinal)
        assertTrue(ScoreTable.ROYALFLUSH.ordinal > ScoreTable.STRAIGHTFLUSH.ordinal)
    }
    /** check if ordinal values are right */
    @Test
    fun testOrdinals() {
        assertEquals(1, ScoreTable.HIGHCARD.ordinal)
        assertEquals(10, ScoreTable.ROYALFLUSH.ordinal)
    }
}