package entity
import kotlin.test.*
/**
 * test cases for game creation
 */
class GameTest {
    /**
     * test default values of a new game
     */
    @Test
    fun testGameInitialization() {
        val p1 = Player("MESSI")
        val p2 = Player("ronaldo")
        val game = Game(players = mutableListOf(p1, p2), totalRounds = 6)
        assertEquals(6, game.totalRounds)
        assertEquals(1, game.currentRound)
        assertEquals(0, game.currentPlayerIndex)
        assertEquals(2, game.players.size)
        assertTrue(game.discardStack.isEmpty())
        assertTrue(game.centerCards.isEmpty())
        assertTrue(game.log.isEmpty())
    }
}