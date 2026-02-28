package service
import entity.Card
import entity.CardValue
import entity.CardSuit
import kotlin.test.*
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
/**
 * test class for the GameService
 * before every test we start new game with 3 player and 3 round
 */
class GameServiceTest {
    /** the main service we use for testing */
    private lateinit var rootService: RootService
    /** fake refreshable to check if we call method */
    private lateinit var testRefreshable: TestRefreshable
    /**
     * prepare new game before each test run
     */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        testRefreshable = TestRefreshable()
        rootService.gameService.addRefreshable(testRefreshable)
        rootService.playerActionService.addRefreshable(testRefreshable)
        rootService.gameService.startNewGame(
            mutableListOf("zidane", "kross", "neuer"),
            3
        )
    }
    /**
     * test if startNewGame create everything correctly
     * check player name, round and card amount
     */
    @Test
    fun testStartNewGame() {
        val game = rootService.currentGame
        checkNotNull(game)
        assertTrue(testRefreshable.refreshAfterStartNewGameCalled)
        assertEquals(3, game.players.size)
        val playerNames = game.players.map { it.name }
        assertTrue(playerNames.contains("zidane"), "zidane ist nicht da")
        assertTrue(playerNames.contains("kross"), "kross ist nicht da")
        assertTrue(playerNames.contains("neuer"), "neuer ist nicht da")
        assertEquals(3, game.totalRounds)
        assertEquals(1, game.currentRound)
        game.players.forEach { player ->
            assertEquals(2, player.hiddenCards.size)
            assertEquals(3, player.openCards.size)
        }
        assertEquals(3, game.centerCards.size)
        assertTrue(game.discardStack.isEmpty())
        // 52 cards - 15 to players - 3 to table = 34
        assertEquals(34, game.drawStack.size)
    }
    /**
     * test if we get error when we start  game while another one run
     */
    @Test
    fun testStartNewGameAlreadyRunning() {
        assertThrows<IllegalStateException> {
            rootService.gameService.startNewGame(
                mutableListOf("zidane", "kross"), 3
            )
        }
    }
    /**
     * test if we get error when there is only 1 player
     */
    @Test
    fun testStartNewGameTooFewPlayers() {
        rootService.currentGame = null
        assertThrows<IllegalArgumentException> {
            rootService.gameService.startNewGame(
                mutableListOf("zidane"), 3
            )
        }
    }
    /**
     * test if we get error when there are 5 player
     */
    @Test
    fun testStartNewGameTooManyPlayers() {
        rootService.currentGame = null
        assertThrows<IllegalArgumentException> {
            rootService.gameService.startNewGame(
                mutableListOf("A", "B", "C", "D", "E"), 3
            )
        }
    }
    /**
     * test if we get error when round count is 0
     */
    @Test
    fun testStartNewGameInvalidRounds() {
        rootService.currentGame = null
        assertThrows<IllegalArgumentException> {
            rootService.gameService.startNewGame(
                mutableListOf("zidane", "kross"), 0
            )
        }
    }
    /**
     * test if we get an error if a player have no name
     */
    @Test
    fun testStartNewGameEmptyName() {
        rootService.currentGame = null
        assertThrows<IllegalArgumentException> {
            rootService.gameService.startNewGame(
                mutableListOf("", "kross"), 3
            )
        }
    }
    /**
     * test if turn go to the next player and action are set to 2
     */
    @Test
    fun testEndTurn() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.currentPlayerIndex = 0
        testRefreshable.reset()
        assertDoesNotThrow { rootService.gameService.endTurn() }
        assertTrue(testRefreshable.refreshAfterTurnEndCalled)
        assertEquals(1, game.currentPlayerIndex)
        assertEquals(2, game.players[1].actionsLeft)
    }
    /**
     * test if turn go back to the first player and round increase
     */
    @Test
    fun testEndTurnWrapsAround() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.currentPlayerIndex = 2
        testRefreshable.reset()
        assertDoesNotThrow { rootService.gameService.endTurn() }
        assertEquals(0, game.currentPlayerIndex)
        assertEquals(2, game.currentRound)
        assertTrue(testRefreshable.refreshAfterTurnEndCalled)
    }
    /**
     * test if endTurn throw error if game is null
     */
    @Test
    fun testEndTurnNoGame() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> {
            rootService.gameService.endTurn()
        }
    }
    /**
     * test if the game end properly, check ranking and set game to null
     */
    @Test
    fun testEndGame() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.players.forEach { player ->
            player.hiddenCards.clear()
            player.openCards.clear()
            player.hiddenCards.add(Card(CardSuit.HEARTS, CardValue.ACE))
            player.hiddenCards.add(Card(CardSuit.HEARTS, CardValue.KING))
            player.openCards.add(Card(CardSuit.HEARTS, CardValue.QUEEN))
            player.openCards.add(Card(CardSuit.HEARTS, CardValue.JACK))
            player.openCards.add(Card(CardSuit.HEARTS, CardValue.TEN))
        }
        testRefreshable.reset()
        assertDoesNotThrow { rootService.gameService.endGame() }
        assertTrue(testRefreshable.refreshAfterGameEndCalled)
        assertNull(rootService.currentGame)
    }
    /**
     * test if endGame throw error if game is null
     */
    @Test
    fun testEndGameNoGame() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> {
            rootService.gameService.endGame()
        }
    }
    /**
     * test if draw stack get the card from the discard stack when it is empty
     */
    @Test
    fun testRefillDrawStackValid() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.drawStack.popAll()
        game.discardStack.popAll()
        game.discardStack.push(Card(CardSuit.HEARTS, CardValue.NINE))
        game.discardStack.push(Card(CardSuit.HEARTS, CardValue.TEN))
        game.discardStack.push(Card(CardSuit.HEARTS, CardValue.QUEEN))
        assertEquals(0, game.drawStack.size)
        assertEquals(3, game.discardStack.size)
        assertDoesNotThrow { rootService.gameService.refillDrawStack() }
        assertTrue(game.drawStack.isNotEmpty())
        assertTrue(game.discardStack.isEmpty())
    }
    /**
     * test if we get error when we try to refill but both stack are empty.
     */
    @Test
    fun testRefillDrawStackBothEmpty() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.drawStack.popAll()
        game.discardStack.popAll()
        assertThrows<IllegalStateException> {
            rootService.gameService.refillDrawStack()
        }
    }

    /**
     * test if refill throw error if game is null
     */
    @Test
    fun testRefillDrawStackNoGame() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> {
            rootService.gameService.refillDrawStack()
        }
    }
    /**
     * test if log message are saved and ui is refreshed
     */
    @Test
    fun testUpdateLog() {
        val game = rootService.currentGame
        checkNotNull(game)
        val message = "test mssage"
        testRefreshable.reset()
        assertDoesNotThrow { rootService.gameService.updateLog(message) }
        assertTrue(testRefreshable.refreshLogCalled)
        assertTrue(game.log.contains(message))
    }
    /**
     * test if updateLog throw an error if game is null
     */
    @Test
    fun testUpdateLogNoGame() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> {
            rootService.gameService.updateLog("test")
        }
    }
}