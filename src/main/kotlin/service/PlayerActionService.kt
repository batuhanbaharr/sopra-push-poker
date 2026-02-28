package service
/**
 * service class for player actions like push or switch
 * @param rootService to access other services and the game state
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * decrease the player's action count by 1
     * end the turn if actions reach 0
     * @throws IllegalStateException if there is no game or no actions left
     */
    private fun reduceAction() {
        checkNotNull(rootService.currentGame) { "es gibt kein spiel am laufen" }
        val game = rootService.currentGame!!
        val player = game.players[game.currentPlayerIndex]
        check(player.actionsLeft > 0) { "Spieler hat keine Aktion mehr" }
        player.actionsLeft--
        if (player.actionsLeft == 0) {
            rootService.gameService.endTurn()
        }
    }
    /**
     * push the center cards to right
     * move the rightmost card to discard stack and draw new one for left side
     * refill the draw stack if its empty
     * @throws IllegalStateException if there is no game or no actions left
     */
    fun pushRight() {
        checkNotNull(rootService.currentGame) { "es gibt kein spiel am laufen" }
        val game = rootService.currentGame!!
        val player = game.players[game.currentPlayerIndex]
        check(player.actionsLeft > 0) { "spieler kann nicht spielen" }
        val rightCard = game.centerCards.removeAt(2)
        game.discardStack.push(rightCard)
        if (game.drawStack.isEmpty()) {
            rootService.gameService.refillDrawStack()
        }
        val newLeftCard = game.drawStack.pop()
        game.centerCards.add(0, newLeftCard)
        rootService.gameService.updateLog("Spieler ${player.name} hat nach rechts geschoben")
        onAllRefreshables { refreshAfterPush(newLeftCard, 1) }
        reduceAction()
    }
    /**
     * push the center cards to left
     * move the leftmost card to the discard stack and draw new one for right side
     * refill the draw stack if its empty
     * @throws IllegalStateException if there is no game or no actions left
     */
    fun pushLeft() {
        checkNotNull(rootService.currentGame) { "es gibt kein spiel am laufen" }
        val game = rootService.currentGame!!
        val player = game.players[game.currentPlayerIndex]
        check(player.actionsLeft > 0) { "Spieler kann nicht spielen" }
        val leftCard = game.centerCards.removeAt(0)
        game.discardStack.push(leftCard)
        if (game.drawStack.isEmpty()) {
            rootService.gameService.refillDrawStack()
        }
        val newRightCard = game.drawStack.pop()
        game.centerCards.add(newRightCard)
        rootService.gameService.updateLog("Spieler ${player.name} hat nach links geschoben")
        onAllRefreshables { refreshAfterPush(newRightCard, 0) }
        reduceAction()
    }
    /**
     * swap one open card of player with one card from center
     * @param openCardIndex index of player's card (0, 1, or 2)
     * @param centerCardIndex index of center card (0, 1, or 2)
     * @throws IllegalStateException if there is no game or no actions left
     * @throws IllegalArgumentException if index is not between 0 and 2
     */
    fun switchOne(openCardIndex: Int, centerCardIndex: Int) {
        checkNotNull(rootService.currentGame) { "es gibt kein spiel am laufen" }
        val game = rootService.currentGame!!
        val player = game.players[game.currentPlayerIndex]
        check(player.actionsLeft > 0) { "Spieler kann nicht spielen" }
        require(openCardIndex in 0..2) { "openCardIndex wert ist ungültig" }
        require(centerCardIndex in 0..2) { "centerCardIndex wert ist ungültig" }
        val centerCard = game.centerCards[centerCardIndex]
        val openCard = player.openCards[openCardIndex]
        game.centerCards[centerCardIndex] = openCard
        player.openCards[openCardIndex] = centerCard
        rootService.gameService.updateLog(
            "Spieler ${player.name} hat eine karte $openCard in seiner hand mit einer karte $centerCard getauscht"
        )
        onAllRefreshables { refreshAfterSwitch() }
        reduceAction()
    }
    /**
     * swap all 3 open cards of player with 3 center cards
     * @throws IllegalStateException if there is no game or no actions left
     */
    fun switchAll() {
        checkNotNull(rootService.currentGame) { "es gibt kein spiel am laufen" }
        val game = rootService.currentGame!!
        val player = game.players[game.currentPlayerIndex]
        check(player.actionsLeft > 0) { "Spieler kann nicht spielen" }
        for (i in 0..2) {
            val cardOnTable = game.centerCards[i]
            val cardInHand = player.openCards[i]
            game.centerCards[i] = cardInHand
            player.openCards[i] = cardOnTable
        }
        rootService.gameService.updateLog("Spieler ${player.name} hat alle Karten in seiner Hand mit allen Karten auf dem Tisch getauscht")
        onAllRefreshables { refreshAfterSwitch() }
        reduceAction()
    }
}