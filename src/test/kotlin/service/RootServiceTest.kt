package service
import kotlin.test.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertNull
import kotlin.test.assertNotNull
/**
 * test for the RootService
 * checks if the main service starts correctly
 */
class RootServiceTest {
    /**
     * test if we can create the RootService without any errors
     */
    @Test
    fun testRootServiceInitialized() {
        val rootService = RootService()
        assertDoesNotThrow { rootService }
    }
    /**
     * test if the current game is empty at the beginning
     */
    @Test
    fun testCurrentGameIsNullAtStart() {
        val rootService = RootService()
        assertNull(rootService.currentGame)
    }
    /**
     * test if the GameService is created successfully
     */
    @Test
    fun testGameServiceExists() {
        val rootService = RootService()
        assertNotNull(rootService.gameService)
    }
    /**
     * test if the playerActionService is created successfully
     */
    @Test
    fun testPlayerActionServiceExists() {
        val rootService = RootService()
        assertNotNull(rootService.playerActionService)
    }
}