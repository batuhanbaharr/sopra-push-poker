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
    /** true when [refreshAfterTurnStart] is called */
    var refreshAfterTurnStartCalled: Boolean = false
        private set
    /** true when [refreshAfterTurnEnd] is called */
    var refreshAfterTurnEndCalled: Boolean = false
        private set
    /** true when [refreshAfterSwitch] is called */
    var refreshAfterSwitchCalled: Boolean = false
        private set
    /** true when [refreshAfterPush] is called */
    var refreshAfterPushCalled: Boolean = false
        private set
    /** true when [refreshAfterError] is called*/
    var refreshAfterErrorCalled: Boolean = false
        private set
    /** true when [refreshLog] is called*/
    var refreshLogCalled: Boolean = false
        private set
    /**
     * make all variables false again
     * use this before new test begin
     */
    fun reset() {
        refreshAfterStartNewGameCalled = false
        refreshAfterGameEndCalled = false
        refreshAfterTurnStartCalled = false
        refreshAfterTurnEndCalled = false
        refreshAfterSwitchCalled = false
        refreshAfterPushCalled = false
        refreshAfterErrorCalled = false
        refreshLogCalled = false
    }
    override fun refreshAfterStartNewGame() {
        refreshAfterStartNewGameCalled = true
    }
    override fun refreshAfterGameEnd(ranking: List<Player>) {
        refreshAfterGameEndCalled = true
    }
    override fun refreshAfterTurnStart() {
        refreshAfterTurnStartCalled = true
    }
    override fun refreshAfterTurnEnd() {
        refreshAfterTurnEndCalled = true
    }
    override fun refreshAfterSwitch() {
        refreshAfterSwitchCalled = true
    }
    override fun refreshAfterPush(newCard: Card, direction: Int) {
        refreshAfterPushCalled = true
    }
    override fun refreshAfterError(message: String) {
        refreshAfterErrorCalled = true
    }
    override fun refreshLog(message: String) {
        refreshLogCalled = true
    }
}