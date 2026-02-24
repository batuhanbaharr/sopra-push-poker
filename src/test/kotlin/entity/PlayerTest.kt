package entity
import kotlin.test.*
/**
 * test cases for player creation and equals
 */
class PlayerTest {
    /**
     * test default values of a new player
     */
    @Test
    fun testPlayerInitialization() {
        val player = Player("Benzema")
        assertEquals("Benzema", player.name)
        assertEquals(2, player.actionsLeft)
        assertTrue(player.openCards.isEmpty())
    }
    /**
     * test equality of identical and different players
     */
    @Test
    fun testEquals() {
        val player1 = Player("Kante")
        val player2 = Player("Kante")
        val player3 = Player("Messi")
        assertEquals(player1, player2)
        assertNotEquals(player1, player3)
    }
}