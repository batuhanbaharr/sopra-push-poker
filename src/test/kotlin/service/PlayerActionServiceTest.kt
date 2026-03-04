package service
import entity.Card
import entity.CardSuit
import entity.CardValue
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
/**
 * test for the PlayerActionService
 * check if the player actions like pushing switching cards work correctly
 */
class PlayerActionServiceTest {
    private lateinit var rootService: RootService
    private lateinit var testRefreshable: TestRefreshable
    /**
     * set up new game with 4 players and the test refreshable before each test
     */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)
        rootService.addRefreshable(testRefreshable)
        rootService.gameService.startNewGame(
            mutableListOf("neymar", "reus", "locatelli", "asensio"),
            3
        )
    }
    /**
     * test if pushing left work correctly
     * check if cards move, action points decrease and the screen refresh
     */
    @Test
    fun testPushLeft() {
        val game = rootService.currentGame
        checkNotNull(game)
        val oldLeftCard = game.centerCards[0]
        val oldMiddleCard = game.centerCards[1]
        val oldRightCard = game.centerCards[2]
        val oldDrawStackSize = game.drawStack.size
        val oldDiscardStackSize = game.discardStack.size
        testRefreshable.reset()
        assertDoesNotThrow { rootService.playerActionService.pushLeft() }
        assertEquals(oldDiscardStackSize + 1, game.discardStack.size)
        assertEquals(oldLeftCard, game.discardStack.peek())
        assertEquals(oldMiddleCard, game.centerCards[0])
        assertEquals(oldRightCard, game.centerCards[1])
        assertEquals(oldDrawStackSize - 1, game.drawStack.size)
        assertTrue(testRefreshable.refreshAfterPushLeftCalled)
        val player = game.players[game.currentPlayerIndex]
        assertEquals(1, player.actionsLeft)
    }
    /**
     * test if pushing left throw error when no game is running
     */
    @Test
    fun testPushLeftNoGame() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> {
            rootService.playerActionService.pushLeft()
        }
    }
    /**
     * test if pushing left throws error when the player has no actions left
     */
    @Test
    fun testPushLeftNoActions() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.players[game.currentPlayerIndex].actionsLeft = 0
        assertThrows<IllegalStateException> {
            rootService.playerActionService.pushLeft()
        }
    }
    /**
     * test if pushing left refill draw stack when it is empty
     */
    @Test
    fun testPushLeftRefillsDrawStack() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.drawStack.popAll()
        game.discardStack.popAll()
        game.discardStack.push(Card(CardSuit.HEARTS, CardValue.NINE))
        game.discardStack.push(Card(CardSuit.HEARTS, CardValue.TEN))
        game.discardStack.push(Card(CardSuit.HEARTS, CardValue.QUEEN))
        assertDoesNotThrow { rootService.playerActionService.pushLeft() }
        assertEquals(3, game.centerCards.size)
    }

    /**
     * test if pushing right works correctly
     * check if cards move, action points decrease and the screen refreshes
     */
    @Test
    fun testPushRight() {
        val game = rootService.currentGame
        checkNotNull(game)
        val oldLeftCard = game.centerCards[0]
        val oldMiddleCard = game.centerCards[1]
        val oldRightCard = game.centerCards[2]
        val oldDrawStackSize = game.drawStack.size
        val oldDiscardStackSize = game.discardStack.size
        testRefreshable.reset()
        assertDoesNotThrow { rootService.playerActionService.pushRight() }
        assertEquals(oldDiscardStackSize + 1, game.discardStack.size)
        assertEquals(oldRightCard, game.discardStack.peek())
        assertEquals(oldLeftCard, game.centerCards[1])
        assertEquals(oldMiddleCard, game.centerCards[2])
        assertEquals(oldDrawStackSize - 1, game.drawStack.size)
        assertTrue(testRefreshable.refreshAfterPushRightCalled)
        val player = game.players[game.currentPlayerIndex]
        assertEquals(1, player.actionsLeft)
    }
    /**
     * test if pushing right throws error when no game is running
     */
    @Test
    fun testPushRightNoGame() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> {
            rootService.playerActionService.pushRight()
        }
    }
    /**
     * test if pushing right throws error when the player has no actions left
     */
    @Test
    fun testPushRightNoActions() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.players[game.currentPlayerIndex].actionsLeft = 0
        assertThrows<IllegalStateException> {
            rootService.playerActionService.pushRight()
        }
    }
    /**
     * test if pushing right refill the draw stack when it is empty
     */
    @Test
    fun testPushRightRefillsDrawStack() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.drawStack.popAll()
        game.discardStack.popAll()
        game.discardStack.push(Card(CardSuit.SPADES, CardValue.ACE))
        game.discardStack.push(Card(CardSuit.SPADES, CardValue.KING))
        game.discardStack.push(Card(CardSuit.SPADES, CardValue.QUEEN))
        assertDoesNotThrow { rootService.playerActionService.pushRight() }
        assertEquals(3, game.centerCards.size)
    }
    /**
     * test if swapping one card between the table and the hand works correctly
     */
    @Test
    fun testSwitchOne() {
        val game = rootService.currentGame
        checkNotNull(game)
        val player = game.players[game.currentPlayerIndex]
        val oldOpenCard = player.openCards[0]
        val oldCenterCard = game.centerCards[0]
        testRefreshable.reset()
        assertDoesNotThrow { rootService.playerActionService.switchOne(0, 0) }
        assertEquals(oldCenterCard, player.openCards[0])
        assertEquals(oldOpenCard, game.centerCards[0])
        assertTrue(testRefreshable.refreshAfterSwitchCalled)
        assertEquals(1, player.actionsLeft)
    }
    /**
     * test if swapping throw error when the card index is wrong
     */
    @Test
    fun testSwitchOneInvalidIndex() {
        assertThrows<IllegalArgumentException> {
            rootService.playerActionService.switchOne(3, 0)
        }
        assertThrows<IllegalArgumentException> {
            rootService.playerActionService.switchOne(0, 3)
        }
    }
    /**
     * test if swapping one card throw error when no game is running
     */
    @Test
    fun testSwitchOneNoGame() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> {
            rootService.playerActionService.switchOne(0, 0)
        }
    }
    /**
     * test if swapping one card throw error when the player has no actions left
     */
    @Test
    fun testSwitchOneNoActions() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.players[game.currentPlayerIndex].actionsLeft = 0
        assertThrows<IllegalStateException> {
            rootService.playerActionService.switchOne(0, 0)
        }
    }
    /**
     * test if switchOne throw error when openCardIndex is negative
     */
    @Test
    fun testSwitchOneNegativeOpenIndex() {
        assertThrows<IllegalArgumentException> {
            rootService.playerActionService.switchOne(-1, 0)
        }
    }
    /**
     * test if switchOne throw error when centerCardIndex is negative
     */
    @Test
    fun testSwitchOneNegativeCenterIndex() {
        assertThrows<IllegalArgumentException> {
            rootService.playerActionService.switchOne(0, -1)
        }
    }
    /**
     * * testt if swapping all three cards works correctly*/
    @Test
    fun testSwitchAll() {
        val game = rootService.currentGame
        checkNotNull(game)
        val player = game.players[game.currentPlayerIndex]
        val oldOpenCards = player.openCards.toList()
        val oldCenterCards = game.centerCards.toList()
        testRefreshable.reset()
        assertDoesNotThrow { rootService.playerActionService.switchAll() }
        for (i in 0..2) {
            assertEquals(oldCenterCards[i], player.openCards[i])
            assertEquals(oldOpenCards[i], game.centerCards[i])
        }
        assertTrue(testRefreshable.refreshAfterSwitchCalled)
        assertEquals(1, player.actionsLeft)
    }
    /**
     * test if swapping all cards throws error when no game is running
     */
    @Test
    fun testSwitchAllNoGame() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> {
            rootService.playerActionService.switchAll()
        }
    }
    /**
     * test if swapping all cards throws error when the player has no actions left
     */
    @Test
    fun testSwitchAllNoActions() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.players[game.currentPlayerIndex].actionsLeft = 0
        assertThrows<IllegalStateException> {
            rootService.playerActionService.switchAll()
        }
    }
}