package gui
import entity.Player
import service.RootService
import service.Refreshable
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.core.BoardGameApplication
/**
 * main app that manage all scenes*/
class SopraApplication : BoardGameApplication("SoPra Game"), Refreshable {
    /** root service for game logic */
    private val rootService = RootService()
    /** menu scene to enter names and rounds */
    private val menuScene = GameMenuScene(rootService)
    /** game scene where you play */
    private val gameScene = GameScene(rootService)
    /** scene between turns to hide cards */
    private val nextPlayerScene = GameNextPlayerScene(rootService)
    /** scene that show final ranking */
    private val finishedScene = GameFinishedScene(rootService)
    /** set up all scenes and register refreshable */
    init {
        rootService.addRefreshable(this)
        rootService.addRefreshable(menuScene)
        rootService.addRefreshable(gameScene)
        rootService.addRefreshable(nextPlayerScene)
        rootService.addRefreshable(finishedScene)
        menuScene.exitButton.onMouseClicked = { exit() }
        finishedScene.exitButton.onMouseClicked = { exit() }
        finishedScene.newGameButton.onMouseClicked = { showMenuScene(menuScene) }
        nextPlayerScene.continueButton.onMouseClicked = {
            rootService.gameService.startTurn()
        }
        showGameScene(gameScene)
        showMenuScene(menuScene)
    }
    /** called when game start, hide menu */
    override fun refreshAfterStartNewGame() {
        showGameScene(gameScene)
        hideMenuScene()
    }
    /** called when turn end, wait little bit then show next player scene */
    override fun refreshAfterTurnEnd() {
        gameScene.lock()
        gameScene.playAnimation(DelayAnimation(675).apply {
            onFinished = {
                gameScene.unlock()
                showMenuScene(nextPlayerScene)
            }
        })
    }
    /** called when game end, show ranking screen */
    override fun refreshAfterGameEnd(ranking: List<Player>) {
        showMenuScene(finishedScene)
    }
    override fun refreshAfterStartTurn() {
        hideMenuScene()
    }
}