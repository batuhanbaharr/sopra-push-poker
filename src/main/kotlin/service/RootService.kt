package service

import entity.Game
/**
 * The root service class is responsible for managing services and the entity layer reference.
 * This class acts as a central hub for every other service within the application.
 *
 */
class RootService{
    /**
     * the service for game actions like start game, end game, evaluate cards*/
    val gameService = GameService(this)
    /**
     * the service for player actions like pushing or swaping cards*/
    val playerActionService = PlayerActionService(this)
    /**
     * currently running game. it can be null if game does not beginn */
    var currentGame: Game? = null
    /**
     * add refreshable to game and playerActionService
     * @param refreshable object to receive refresh updates*/
    fun addRefreshable(refreshable: Refreshable) {
        gameService.addRefreshable(refreshable)
        playerActionService.addRefreshable(refreshable)
    }
}