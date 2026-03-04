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
    /** called after new game start */
    fun refreshAfterStartNewGame() {}
    /**
     * called after the game end
     * @param ranking list of players sorted from best to worst hand*/
    fun refreshAfterGameEnd(ranking: List<Player>) {}
    /** called after player's turn end*/
    fun refreshAfterTurnEnd(){}
    /** called after player swap cards with center */
    fun refreshAfterSwitch() {}
    /**
     * called after center cards are pushed to right
     * @param newCard new card that was added on left side
     */
    fun refreshAfterPushRight(newCard: Card) {}
    /**
     * called after center cards are pushed to left
     * @param newCard the new card that was added on right side
     */
    fun refreshAfterPushLeft(newCard: Card){}
    /**
     * called when something goes wrong during player action
     * @param message description of error
     */
    fun refreshAfterError(message: String){}
    /**
     * called after new message is added the game log
     * @param message message that was added
     */
    fun refreshLog(message: String){}
    fun refreshAfterStartTurn(){}
}
