package entity
import kotlin.test.*
/**
 * test cases for player creation and equals
 */
class PlayerTest {
    @Test
    fun testPlayerInitialization() {
        val player = Player("Benzema")
        assertEquals("Benzema", player.name)
        assertEquals(2, player.actionsLeft)
        assertTrue(player.openCards.isEmpty())
    }
    @Test
    fun testEquals() {
        val player1 = Player("Kante")
        val player2 = Player("Kante")
        assertEquals(player1, player2)
        assertNotSame(player1, player2)
    }
}