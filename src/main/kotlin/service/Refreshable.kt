package service
import entity.Card
import entity.Player
/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the GUI classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * GUI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 */

interface Refreshable {
    fun refreshAfterStartNewGame() {}
    fun refreshAfterGameEnd(ranking: List<Player>) {}
    fun refreshAfterTurnStart() {}
    fun refreshAfterTurnEnd() {}
    fun refreshAfterSwitch() {}
    fun refreshAfterPush(newCard: Card, direction: Int) {}
    fun refreshAfterError(message: String) {}
    fun refreshLog(message: String) {}
}