package entity
import kotlin.test.*
/**
 * test cases for game creation
 */
class GameTest {
    @Test
    fun testGameInitialization() {
        val game = Game(totalRounds = 6)
        assertEquals(6, game.totalRounds)
        assertEquals(6, game.currentRound)
        assertEquals(0, game.currentPlayerIndex)
        assertTrue(game.players.isEmpty())
        assertTrue(game.discardStack.isEmpty())
        assertTrue(game.centerCards.isEmpty())
        assertTrue(game.log.isEmpty())
    }
}