package entity

import kotlin.test.*

class GameTest {
    @Test
    fun testGameInitialization() {
        val game = Game(nRounds = 3)
        assertEquals(3, game.nRounds)
        assertEquals(0, game.curPlayerIdx)
        assertTrue(game.players.isEmpty())
        assertTrue(game.playStack.isEmpty())
        assertTrue(game.centerCards.isEmpty())
        assertTrue(game.log.isEmpty())
    }
    @Test
    fun testEquals() {
        val game1 = Game(nRounds = 5)
        val game2 = Game(nRounds = 5)
        assertEquals(game1, game2)
        assertNotSame(game1, game2)
    }
}