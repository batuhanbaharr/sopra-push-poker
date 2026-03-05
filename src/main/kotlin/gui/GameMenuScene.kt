package gui
import service.RootService
import service.Refreshable
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
/**
 * menu to type player names and pick rounds
 * @param rootService for game logic*/
class GameMenuScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {
    /** gold border*/
    private val borderPane = Pane<UIComponent>(posX = 1920 / 2 - 354, posY = 1080 / 2 - 454, width = 708,
        height = 908, visual = ColorVisual(212, 175, 55))
    /** dark box in middle */
    private val contentPane = Pane<UIComponent>(posX = 1920 / 2 - 350, posY = 1080 / 2 - 450,
        width = 700, height = 900, visual = ColorVisual(15, 45, 20))
    /** title */
    private val titleLabel = Label(posX = 0, posY = 20, width = 700, height = 80, text = "♥ Schiebe Poker ♦",
        font = Font(size = 48, color = Color(212, 175, 55)), alignment = Alignment.CENTER)
    /** small text under title */
    private val subtitleLabel = Label(posX = 0, posY = 100, width = 700, height = 30,
        text = "Spieler eingeben und Runden auswählen", font = Font(size = 16,
            color = Color(160, 160, 140)), alignment = Alignment.CENTER)
    /** line under title */
    private val topDivider = Label(posX = 100, posY = 145, width = 500, height = 2,
        visual = ColorVisual(212, 175, 55))
    /** player 1 label */
    private val p1Label = Label(posX = 50, posY = 180, width = 150, height = 50, text = "Spieler 1:",
        font = Font(size = 18, color = Color(212, 175, 55)), alignment = Alignment.CENTER_LEFT)
    /** player 1 name input */
    private val p1Input = TextField(posX = 210, posY = 180, width = 440, height = 50, prompt = "Name eingeben",
        font = Font(size = 16, color = Color(255, 255, 255))).apply{
            visual = ColorVisual(30, 70, 40) }
    /** player 2 label */
    private val p2Label = Label(posX = 50, posY = 250, width = 150, height = 50, text = "Spieler 2:",
        font = Font(size = 18, color = Color(212, 175, 55)), alignment = Alignment.CENTER_LEFT)
    /** player 2 name input */
    private val p2Input = TextField(posX = 210, posY = 250, width = 440, height = 50, prompt = "Name eingeben",
        font = Font(size = 16, color = Color(255, 255, 255))).apply{
            visual = ColorVisual(30, 70, 40) }
    /** player 3 label */
    private val p3Label = Label(posX = 50, posY = 320, width = 150, height = 50, text = "Spieler 3:",
        font = Font(size = 18, color = Color(212, 175, 55)),
        alignment = Alignment.CENTER_LEFT)
    /** player 3 name input (optional) */
    private val p3Input = TextField(posX = 210, posY = 320, width = 440, height = 50,
        prompt = "(optional)", font = Font(size = 16, color = Color(255, 255, 255))
    ).apply { visual = ColorVisual(30, 70, 40) }
    /** player 4 label */
    private val p4Label = Label(posX = 50, posY = 390, width = 150, height = 50, text = "Spieler 4:",
        font = Font(size = 18, color = Color(212, 175, 55)), alignment = Alignment.CENTER_LEFT)
    /** player 4 name input (optional) */
    private val p4Input = TextField(posX = 210, posY = 390, width = 440, height = 50,
        prompt = "(optional)", font = Font(size = 16, color = Color(255, 255, 255))).apply{
            visual = ColorVisual(30, 70, 40) }
    /** line above round picker */
    private val midDivider = Label(posX = 100, posY = 470, width = 500, height = 2,
        visual = ColorVisual(212, 175, 55))
    /** round label */
    private val roundLabel = Label(posX = 50, posY = 500, width = 150, height = 50, text = "Runden:",
        font = Font(size = 18, color = Color(212, 175, 55)), alignment = Alignment.CENTER_LEFT)
    /** dropdown to pick round count*/
    private val roundBox = ComboBox(posX = 210, posY = 500, width = 440,
        height = 50, items = listOf(2, 3, 4, 5, 6, 7), prompt = "Runden auswählen",
        font = Font(size = 16)).apply { visual = ColorVisual(30, 70, 40) }
    /** show error text here */
    private val errorLabel = Label(posX = 0, posY = 580, width = 700, height = 30, text = "",
        font = Font(size = 14, color = Color(220, 80, 80)), alignment = Alignment.CENTER)
    /** start button */
    private val startButton = Button(posX = 50, posY = 630, width = 290, height = 60, text = "♠ STARTEN",
        font = Font(size = 22, color = Color(15, 45, 20)),
        visual = ColorVisual(212, 175, 55)).apply {
        onMouseEntered = { visual = ColorVisual(240, 200, 80) }
        onMouseExited = { visual = ColorVisual(212, 175, 55) }
        onMouseClicked = {
            val names = mutableListOf<String>()
            if (p1Input.text.isNotBlank()) names.add(p1Input.text)
            if (p2Input.text.isNotBlank()) names.add(p2Input.text)
            if (p3Input.text.isNotBlank()) names.add(p3Input.text)
            if (p4Input.text.isNotBlank()) names.add(p4Input.text)
            val rounds = roundBox.selectedItem
            if (names.size < 2) {
                errorLabel.text = "Mindestens 2 Spieler eingeben"
            } else if (rounds == null) {
                errorLabel.text = "Rundenanzahl auswählen"
            } else {
                errorLabel.text = ""
                rootService.gameService.startNewGame(names.toMutableList(), rounds)
            }
        }
    }
    /** exit button */
    val exitButton = Button(posX = 360, posY = 630, width = 290, height = 60, text = "BEENDEN",
        font = Font(size = 22, color = Color(212, 175, 55)),
        visual = ColorVisual(80, 30, 30)).apply {
        onMouseEntered = { visual = ColorVisual(110, 40, 40) }
        onMouseExited = { visual = ColorVisual(80, 30, 30) }
    }
    /** line above footer*/
    private val bottomDivider = Label(posX = 100, posY = 720, width = 500, height = 2,
        visual = ColorVisual(212, 175, 55))
    /**footer text*/
    private val footerLabel = Label(posX = 0, posY = 740, width = 700, height = 40,
        text = "♣ ♦ ♥ ♠  Schiebe-Poker  ♠ ♥ ♦ ♣",
        font = Font(size = 18, color = Color(120, 100, 60)), alignment = Alignment.CENTER)
    init {
        background = ColorVisual(10, 30, 15)
        contentPane.addAll(titleLabel, subtitleLabel, topDivider,
            p1Label, p1Input, p2Label, p2Input, p3Label, p3Input, p4Label, p4Input, midDivider,
            roundLabel, roundBox, errorLabel, startButton, exitButton, bottomDivider, footerLabel)
        addComponents(borderPane, contentPane)
    }
}