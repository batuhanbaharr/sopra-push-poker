package gui
import entity.Player
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
 * show final ranking when game end
 * @param rootService for game logic*/
class GameFinishedScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {
    /** dark box in middle */
    private val contentPane = Pane<UIComponent>(posX = 1920/2-350, posY = 1080/2-370, width = 700,
        height = 740, visual = ColorVisual(15, 45, 20))
    /** trophy icon */
    private val trophyLabel = Label(posX = 0, posY = 30, width = 700, height = 80, text = "🏆",
        font = Font(size = 64), alignment = Alignment.CENTER)
    /** result title */
    private val titleLabel = Label(posX = 0, posY = 110, width = 700, height = 50, text = "♛ ERGEBNIS ♛",
        font = Font(size = 36, color = Color(212, 175, 55)), alignment = Alignment.CENTER)
    /** show winner name */
    private val winnerLabel = Label(posX = 0, posY = 160, width = 700, height = 60, text = "",
        font = Font(size = 32, color = Color(255, 215, 0)), alignment = Alignment.CENTER)
    private val topLine = Label(posX = 100, posY = 230, width = 500, height = 2,
        visual = ColorVisual(212, 175, 55))
    /** 1 place label */
    private val rank1 = Label(posX = 50, posY = 250, width = 600, height = 50, text = "",
        font = Font(size = 26, color = Color(255, 215, 0)), alignment = Alignment.CENTER)
    /** 2 place label */
    private val rank2 = Label(posX = 50, posY = 310, width = 600, height = 50, text = "",
        font = Font(size = 22, color = Color(192, 192, 192)),
        alignment = Alignment.CENTER)
    /** 3 place label */
    private val rank3 = Label(posX = 50, posY = 370, width = 600, height = 50, text = "",
        font = Font(size = 20, color = Color(205, 127, 50)), alignment = Alignment.CENTER)
    /** 4 place label */
    private val rank4 = Label(posX = 50, posY = 430, width = 600, height = 50, text = "",
        font = Font(size = 18, color = Color(160, 160, 140)), alignment = Alignment.CENTER)
    /** all rank label in list */
    private val ranks = listOf(rank1, rank2, rank3, rank4)
    private val bottomLine = Label(posX = 100, posY = 500, width = 500, height = 2,
        visual = ColorVisual(212, 175, 55))
    /** button to start new game */
    val newGameButton = Button(posX = 50, posY = 530, width = 290, height = 60, text = "♠ NEUES SPIEL",
        font = Font(size = 22, color = Color(15, 45, 20)),
        visual = ColorVisual(212, 175, 55)).apply {
        onMouseEntered = { visual = ColorVisual(240, 200, 80) }
        onMouseExited = { visual = ColorVisual(212, 175, 55) }
    }
    /** button to clos app */
    val exitButton = Button(posX = 360, posY = 530, width = 290, height = 60,
        text = "BEENDEN", font = Font(size = 22, color = Color(212, 175, 55)),
        visual = ColorVisual(80, 30, 30)).apply {
        onMouseEntered = { visual = ColorVisual(110, 40, 40) }
        onMouseExited = { visual = ColorVisual(80, 30, 30) }
    }
    /** penguin dance :) at bottom */
    private val footerLabel = Label(posX = 0, posY = 620, width = 700, height = 40,
        text = "🐧 ♠ 🐧 ♥ 🐧 ♦ 🐧 ♣ 🐧 ♠ 🐧 ♥ 🐧 ♦ 🐧 ♣ 🐧",
        font = Font(size = 22, color = Color(120, 100, 60)), alignment = Alignment.CENTER)
    /** add all component to scene */
    init {
        background = ColorVisual(10, 30, 15)
        contentPane.addAll(trophyLabel, titleLabel, winnerLabel, topLine, rank1, rank2,
            rank3, rank4, bottomLine, newGameButton, exitButton, footerLabel)
        addComponents(contentPane)
    }
    /**
     * fill ranking label when game end
     * @param ranking player list sorted best to worst */
    override fun refreshAfterGameEnd(ranking: List<Player>) {
        if (ranking.isNotEmpty()) winnerLabel.text = "🏆 ${ranking[0].name} gewinnt 🏆"
        val medals = listOf("🥇", "🥈", "🥉", "  4.")
        for (i in ranks.indices) {
            if (i < ranking.size) {
                ranks[i].text = "${medals[i]}  ${ranking[i].name}  —  ${ranking[i].score}"
                ranks[i].isVisible = true
            } else {
                ranks[i].isVisible = false
            }
        }
    }
}