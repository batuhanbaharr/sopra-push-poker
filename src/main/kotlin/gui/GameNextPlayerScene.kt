package gui
import service.RootService
import service.Refreshable
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
/**
 * screen between turns to hide cards from next player
 * @param rootService for game logic*/
class GameNextPlayerScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {
    /** dark box in middle */
    private val contentPane = Pane<UIComponent>(posX = 1920 / 2 - 300, posY = 1080 / 2 - 250, width = 600, height = 500,
        visual = ColorVisual(15, 45, 20))
    /** card suit decoration*/
    private val topDeco = Label(posX = 0, posY = 20, width = 600, height = 40, text = "♠ ♥ ♦ ♣",
        font = Font(size = 30, color = Color(212, 175, 55)), alignment = Alignment.CENTER)
    private val topLine = Label(posX = 75, posY = 70, width = 450, height = 2, visual = ColorVisual(212, 175, 55))
    /** info text */
    private val infoLabel = Label(posX = 0, posY = 100, width = 600, height = 50, text = "Nächster Spieler:",
        font = Font(size = 22, color= Color(160, 160, 140)),
        alignment = Alignment.CENTER)
    /** player name */
    private val nameLabel = Label(posX = 0, posY = 160, width = 600, height = 80, text = "",
        font = Font(size = 42, color = Color(212, 175, 55)), alignment = Alignment.CENTER)
    /** round info */
    private val roundLabel = Label(posX = 0, posY = 250, width = 600, height = 40, text = "",
        font = Font(size = 18, color = Color(160, 160, 140)), alignment = Alignment.CENTER)
    /** gold line above button */
    private val bottomLine =
        Label(posX = 75, posY = 310, width = 450, height = 2, visual = ColorVisual(212, 175, 55))
    /** continue button */
    val continueButton = Button(posX = 100, posY = 340, width = 400, height = 60, text = "♠ WEITER ♠",
        font = Font(size = 24, color = Color(15, 45, 20)),
        visual = ColorVisual(212, 175, 55)).apply {
        onMouseEntered = { visual = ColorVisual(240, 200, 80) }
        onMouseExited = { visual = ColorVisual(212, 175, 55) }
    }
    /** card suit decoration */
    private val bottomDeco = Label(posX = 0, posY = 430, width = 600, height = 40, text = "♣ ♦ ♥ ♠",
        font = Font(size = 30, color = Color(120, 100, 60)), alignment = Alignment.CENTER)
    /** add everything to scene*/
    init {
        background = ColorVisual(10, 30, 15)
        contentPane.addAll(topDeco, topLine, infoLabel, nameLabel, roundLabel, bottomLine,
            continueButton, bottomDeco)
        addComponents(contentPane)
    }
    /** called when turn end, show next player name and round */
    override fun refreshAfterTurnEnd() {
        val game = rootService.currentGame
        checkNotNull(game) { "es gibt kein spiel" }
        nameLabel.text = game.players[game.currentPlayerIndex].name
        roundLabel.text = "Runde ${game.currentRound} von ${game.totalRounds}"
    }
}