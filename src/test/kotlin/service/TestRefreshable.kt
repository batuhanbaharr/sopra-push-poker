package service
import entity.Card
import entity.Player
/**
 * helper class for testing
 *  it just remember if  refresh method was called
 * it does not change the screen
 * use reset to make all variables false again
 */
class TestRefreshable : Refreshable {
    /** true when [refreshAfterStartNewGame] is called */
    var refreshAfterStartNewGameCalled: Boolean = false
        private set
    /** true when [refreshAfterGameEnd] is called */
    var refreshAfterGameEndCalled: Boolean = false
        private set
    /** true when [refreshAfterStartTurn] is called */
    var refreshAfterStartTurnCalled: Boolean = false
        private set
    /** true when [refreshAfterTurnEnd] is called */
    var refreshAfterTurnEndCalled: Boolean = false
        private set
    /** true when [refreshAfterSwitch] is called */
    var refreshAfterSwitchCalled: Boolean = false
        private set
    /** true when [refreshAfterPushRight] is called */
    var refreshAfterPushRightCalled: Boolean = false
        private set
    /** true when [refreshAfterPushLeft] is called */
    var refreshAfterPushLeftCalled: Boolean = false
        private set
    /** true when [refreshAfterError] is called*/
    var refreshAfterErrorCalled: Boolean = false
        private set
    /** true when [refreshLog] is called*/
    var refreshLogCalled: Boolean = false
        private set
    /**
     * make all variables false again
     * use this before new test begiin*/
    fun reset() {
        refreshAfterStartNewGameCalled = false
        refreshAfterGameEndCalled = false
        refreshAfterStartTurnCalled = false
        refreshAfterTurnEndCalled = false
        refreshAfterSwitchCalled = false
        refreshAfterPushRightCalled = false
        refreshAfterPushLeftCalled = false
        refreshAfterErrorCalled = false
        refreshLogCalled = false
    }
    override fun refreshAfterStartNewGame() {
        refreshAfterStartNewGameCalled = true
    }
    override fun refreshAfterGameEnd(ranking: List<Player>) {
        refreshAfterGameEndCalled = true
    }
    override fun refreshAfterStartTurn() {
        refreshAfterStartTurnCalled = true
    }
    override fun refreshAfterTurnEnd() {
        refreshAfterTurnEndCalled = true
    }
    override fun refreshAfterSwitch() {
        refreshAfterSwitchCalled = true
    }
    override fun refreshAfterPushRight(newCard: Card) {
        refreshAfterPushRightCalled = true
    }
    override fun refreshAfterPushLeft(newCard: Card) {
        refreshAfterPushLeftCalled = true
    }
    override fun refreshAfterError(message: String) {
        refreshAfterErrorCalled = true
    }
    override fun refreshLog(message: String) {
        refreshLogCalled = true
    }
}