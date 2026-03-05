package gui
import entity.Card
import entity.Game
import service.RootService
import service.Refreshable
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
/**
 * game scene that show cards and buttons
 * @param rootService for game logic*/
class GameScene(private val rootService: RootService):
    BoardGameScene(1920, 1080, background = ColorVisual(20, 70, 35)), Refreshable {
    /** load card images */
    private val cardImageLoader = CardImageLoader()
    /** save log messages */
    private val logMessages = mutableListOf<String>()
    /** how far we scrolled in the log */
    private var logOffset = 0
    /** index of selected open card, -1 mean nothing picked*/
    private var pickedOpenIndex = -1
    /** index of selected center card, -1 mean nothing picked */
    private var pickedCenterIndex = -1
    /** store open card views for highlight */
    private val openViews = mutableListOf<CardView>()
    /** store center card views for highlight */
    private val centerViews = mutableListOf<CardView>()
    /** show other player cards face down */
    private val otherPlayersLabels = mutableListOf<Label>()
    private val otherPlayersLayouts = mutableListOf<LinearLayout<CardView>>()
    /** round info */
    private val roundLabel = Label(posX = 20, posY = 15, width = 300, height = 40, text = "Runde 1 / 3",
        font = Font(size = 22, color = Color(212, 175, 55)), alignment = Alignment.CENTER_LEFT)
    /** player name */
    private val playerLabel = Label(posX = 1920 / 2 - 200, posY = 15, width = 400, height = 40,
        text = "", font = Font(size = 26, color = Color(212, 175, 55)),
        alignment = Alignment.CENTER)
    /** how many actions left */
    private val actionsLabel = Label(posX = 1920 - 250, posY = 15, width = 230, height = 40, text = "Aktionen: 2",
        font = Font(size = 22, color = Color(212, 175, 55)),
        alignment = Alignment.CENTER_RIGHT)
    /** gold line under top */
    private val topLine = Label(posX = 20, posY = 60, width = 1880, height = 2, visual = ColorVisual(212, 175, 55))
    /** text above center cards */
    private val centerLabel = Label(posX = 1920 / 2 - 150, posY = 250, width = 300, height = 30,
        text = "Karten auf dem Tisch",
        font = Font(size = 18, color = Color(160, 160, 140)),
        alignment = Alignment.CENTER)
    /** push left arrow */
    private val pushLeftBtn = Button(posX = 1920 / 2 - 330, posY = 330, width = 60, height = 200, text = "◄",
        font = Font(size = 36, color = Color(15, 45, 20)),
        visual = ColorVisual(212, 175, 55)).apply {
        onMouseEntered = { visual = ColorVisual(240, 200, 80) }
        onMouseExited = { visual = ColorVisual(212, 175, 55) }
        onMouseClicked = { rootService.playerActionService.pushLeft() }
    }
    /** 3 center cards */
    private val centerLayout = LinearLayout<CardView>(posX = 1920 / 2 - 250, posY = 290, width = 500, height = 200,
        spacing = 30, alignment = Alignment.CENTER)
    /** push right arrow */
    private val pushRightBtn = Button(posX = 1920 / 2 + 270, posY = 330, width = 60, height = 200,
        text = "►", font = Font(size = 36, color = Color(15, 45, 20)),
        visual = ColorVisual(212, 175, 55)).apply {
        onMouseEntered = { visual = ColorVisual(240, 200, 80) }
        onMouseExited = { visual = ColorVisual(212, 175, 55) }
        onMouseClicked = { rootService.playerActionService.pushRight() }
    }
    private val drawLabel = Label(posX = 350, posY = 290, width = 190, height = 40, text = "Nachziehstapel",
        font = Font(size = 18, color = Color(212, 175, 55)),
        alignment = Alignment.CENTER)
    private val drawCard = CardView(posX = 380, posY = 340, width = 130, height = 200, front = cardImageLoader.backImage,
        back = cardImageLoader.backImage)
    private val drawCount = Label(posX = 350, posY = 545, width = 190, height = 30, text = "Karten: 0",
        font = Font(size = 16, color = Color(160, 160, 140)), alignment = Alignment.CENTER)
    private val discardLabel = Label(posX = 1920 - 540, posY = 290, width = 190, height = 40, text = "Ablagestapel",
        font = Font(size = 18, color = Color(212, 175, 55)), alignment = Alignment.CENTER)
    private val discardCard = CardView(posX = 1920 - 510, posY = 340, width = 130, height = 200,
        front = cardImageLoader.blankImage, back = cardImageLoader.blankImage)
    private val discardCount = Label(posX = 1920 - 540, posY = 545, width = 190, height = 30,
        text = "Karten: 0", font = Font(size = 16, color = Color(160, 160, 140)),
        alignment = Alignment.CENTER)
    /** text above open cards */
    private val openLabel = Label(posX = 1920 / 2 - 400, posY = 620, width = 300, height = 30, text = "Offene Karten",
        font = Font(size = 18, color = Color(160, 160, 140)), alignment = Alignment.CENTER)
    /**3 open cards layout */
    private val openLayout = LinearLayout<CardView>(posX = 1920 / 2 - 400, posY = 660, width = 500, height = 200,
        spacing = 20, alignment = Alignment.CENTER)
    /** text above hidden cards*/
    private val hiddenLabel = Label(posX = 1920 / 2 + 200, posY = 620, width = 300, height = 30,
        text = "Verdeckte Karten", font = Font(size = 18, color = Color(160, 160, 140)),
        alignment = Alignment.CENTER)
    /** 2 hidden cards layout */
    private val hiddenLayout = LinearLayout<CardView>(posX = 1920 / 2 + 200, posY = 660, width = 350, height = 200,
        spacing = 20, alignment = Alignment.CENTER)
    /** gold line above buttons */
    private val bottomLine = Label(posX = 20, posY = 880, width = 1880, height = 2,
        visual = ColorVisual(212, 175, 55))
    /** swap all 3 cards */
    private val swapAllBtn = Button(posX = 1920 / 2 - 220, posY = 895, width = 200, height = 45, text = "Alle tauschen",
        font = Font(size = 16, color = Color(15, 45, 20)),
        visual = ColorVisual(212, 175, 55)).apply {
        onMouseEntered = { visual = ColorVisual(240, 200, 80) }
        onMouseExited = { visual = ColorVisual(212, 175, 55) }
        onMouseClicked = { rootService.playerActionService.switchAll() }
    }
    /** swap one card */
    private val swapOneBtn = Button(posX = 1920 / 2 + 20, posY = 895, width = 200, height = 45,
        text = "Eine tauschen", font = Font(size = 16, color = Color(15, 45, 20)),
        visual = ColorVisual(212, 175, 55)).apply {
        onMouseEntered = { visual = ColorVisual(240, 200, 80) }
        onMouseExited = { visual = ColorVisual(212, 175, 55) }
        onMouseClicked = {
            if (pickedOpenIndex in 0..2 && pickedCenterIndex in 0..2) {
                rootService.playerActionService.switchOne(pickedOpenIndex, pickedCenterIndex)
                pickedOpenIndex = -1
                pickedCenterIndex = -1
            }
        }
    }
    /** show which cards are picked */
    private val pickInfo = Label(posX = 1920 / 2 + 230, posY = 895, width = 250, height = 45, text = "",
        font = Font(size = 14, color = Color(212, 175, 55)), alignment = Alignment.CENTER_LEFT)
    /** skip turn button */
    private val skipBtn = Button(posX = 1920 - 230, posY = 895, width = 200, height = 45, text = "Runde beenden",
        font = Font(size = 14, color = Color(212, 175, 55)),
        visual = ColorVisual(80, 30, 30)).apply {
        onMouseEntered = { visual = ColorVisual(110, 40, 40) }
        onMouseExited = { visual = ColorVisual(80, 30, 30) }
        onMouseClicked = { rootService.gameService.endTurn() }
    }
    /** line above log */
    private val logLine = Label(posX = 20, posY = 950, width = 1880, height = 2,
        visual = ColorVisual(212, 175, 55))
    /** log title */
    private val logTitle = Label(posX = 20, posY = 960, width = 100, height = 30,
        text = "♠ Log:", font = Font(size = 18, color = Color(212, 175, 55)),
        alignment = Alignment.CENTER_LEFT)
    private val log1 = Label(posX = 130, posY = 958, width = 1770, height = 25,
        text = "", font = Font(size = 18, color = Color(200, 200, 180)),
        alignment = Alignment.CENTER_LEFT)
    private val log2 = Label(posX = 130, posY = 983, width = 1770, height = 25,
        text = "", font = Font(size = 18, color = Color(170, 170, 150)), alignment = Alignment.CENTER_LEFT)
    private val log3 = Label(posX = 130, posY = 1008, width = 1770, height = 25, text = "",
        font = Font(size = 18, color = Color(140, 140, 120)), alignment = Alignment.CENTER_LEFT)
    private val log4 = Label(posX = 130, posY = 1033, width = 1770, height = 25,
        text = "", font = Font(size = 18, color = Color(110, 110, 90)),
        alignment = Alignment.CENTER_LEFT)
    private val logRows = listOf(log1, log2, log3, log4)
    /** add all components to the scene */
    init {
        addComponents(roundLabel, playerLabel, actionsLabel, topLine, centerLabel, pushLeftBtn,
            centerLayout, pushRightBtn, drawLabel, drawCard, drawCount, discardLabel,
            discardCard, discardCount, openLabel, openLayout, hiddenLabel, hiddenLayout,
            bottomLine, swapAllBtn, swapOneBtn, pickInfo, skipBtn, logLine, logTitle, log1, log2, log3, log4)
    }
    /**
     * make a card view for one card
     * @param card which card
     */
    private fun makeCard(card: Card): CardView {
        val cv = CardView(posX = 0, posY = 0, width = 130, height = 200,
            front = cardImageLoader.frontImageFor(card.suit, card.value), back = cardImageLoader.backImage)
        cv.showFront()
        cv.onMouseEntered = { cv.scale = 1.08 }
        cv.onMouseExited = { if (isHighlighted(cv)) cv.scale = 1.15 else cv.scale = 1.0 }
        return cv
    }
    /**
     * check if this card is selected right now
     * @param cv (CARD VIEW) to check*/
    private fun isHighlighted(cv: CardView): Boolean {
        if (pickedOpenIndex in 0..2 && openViews.size > pickedOpenIndex) {
            if (openViews[pickedOpenIndex] == cv) return true
        }
        if (pickedCenterIndex in 0..2 && centerViews.size > pickedCenterIndex) {
            if (centerViews[pickedCenterIndex] == cv) return true
        }
        return false
    }
    /** remove highlight from all card views */
    private fun clearHighlights() {
        for (cv in openViews) cv.scale = 1.0
        for (cv in centerViews) cv.scale = 1.0
    }
    /** reset all picked cards back to nothing */
    private fun resetPicks() {
        clearHighlights()
        pickedOpenIndex = -1
        pickedCenterIndex = -1
        pickInfo.text = ""
    }
    /**
     * show open and hidden cards of current player
     * @param game the current game*/
    private fun showHand(game: Game) {
        openLayout.clear()
        hiddenLayout.clear()
        openViews.clear()
        val player = game.players[game.currentPlayerIndex]
        for (i in player.openCards.indices) {
            val cv = makeCard(player.openCards[i])
            cv.onMouseClicked = {
                if (pickedOpenIndex >= 0 && pickedOpenIndex < openViews.size) {
                    openViews[pickedOpenIndex].scale = 1.0
                }
                pickedOpenIndex = i
                cv.scale = 1.15
                updatePickInfo()
            }
            openViews.add(cv)
            openLayout.add(cv)
        }
        for (card in player.hiddenCards) {
            hiddenLayout.add(makeCard(card))
        }
    }
    /**
     ** show the 3 center cards on table
     * @param game the current game*/
    private fun showCenter(game: Game) {
        centerLayout.clear()
        centerViews.clear()
        for (i in game.centerCards.indices) {
            val cv = makeCard(game.centerCards[i])
            cv.onMouseClicked = {
                if (pickedCenterIndex >= 0 && pickedCenterIndex < centerViews.size) {
                    centerViews[pickedCenterIndex].scale = 1.0
                }
                pickedCenterIndex = i
                cv.scale = 1.15
                updatePickInfo()
            }
            centerViews.add(cv)
            centerLayout.add(cv)
        }
    }
    /** show other players open cards face up and hidden cards face down */
    private fun showOthers(game: Game) {
        for (label in otherPlayersLabels) removeComponents(label)
        for (layout in otherPlayersLayouts) removeComponents(layout)
        otherPlayersLabels.clear()
        otherPlayersLayouts.clear()
        val others = mutableListOf<Int>()
        for (i in game.players.indices) {
            if (i != game.currentPlayerIndex) others.add(i)
        }
        val spots = when (others.size) {
            1 -> listOf("top")
            2 -> listOf("top", "left")
            3 -> listOf("top", "left", "right")
            else -> listOf()
        }
        for (j in others.indices) {
            val player = game.players[others[j]]
            val spot = spots[j]
            var lx = 0; var ly = 0; var lw = 400; var rot = 0.0
            var nx = 0; var ny = 0; var nw = 400
            when (spot) {"top" -> { nx = 1920 / 2 - 200; ny = 70; lx = 1920 / 2 - 200; ly = 100; lw = 500 }
                "left" -> { nx = 130; ny = 300; nw = 150; rot = 90.0; lx = -200; ly = 280; lw = 600 }
                "right" -> { nx = 1920-280; ny = 300; nw = 150; rot = -90.0; lx = 1920-400; ly = 280; lw = 600 }
            }
            val nameLabel = Label(posX = nx, posY = ny, width = nw, height = 30, text = player.name,
                font = Font(size = if (spot == "top") 16 else 14, color = Color(212, 175, 55)),
                alignment = Alignment.CENTER).apply { rotation = rot }
            val layout = LinearLayout<CardView>(posX = lx, posY = ly, width = lw, height = 150, spacing = -25,
                alignment = Alignment.CENTER).apply { rotation = rot }
            for (card in player.openCards) {
                val cv = CardView(posX = 0, posY = 0, width = 90, height = 135,
                    front = cardImageLoader.frontImageFor(card.suit, card.value),
                    back = cardImageLoader.backImage)
                cv.showFront()
                layout.add(cv)
            }
            repeat(2) {
                layout.add(CardView(posX = 0, posY = 0, width = 90,
                    height = 135, front = cardImageLoader.backImage, back = cardImageLoader.backImage))
            }
            otherPlayersLabels.add(nameLabel)
            otherPlayersLayouts.add(layout)
            addComponents(nameLabel, layout)
        }
    }
    /**
     * update discard stack to show top card
     * @param game current game */
    private fun updateDiscard(game: Game) {
        if (game.discardStack.isNotEmpty()) {
            val top = game.discardStack.peek()
            discardCard.frontVisual = cardImageLoader.frontImageFor(top.suit, top.value)
            discardCard.showFront()
        } else {
            discardCard.frontVisual = cardImageLoader.blankImage
            discardCard.showFront()
        }
        discardCount.text = "Karten: ${game.discardStack.size}"
    }
    /**
     * update round player and action labels
     * @param game current game*/
    private fun updateInfo(game: Game) {
        val player = game.players[game.currentPlayerIndex]
        roundLabel.text = "Runde ${game.currentRound} / ${game.totalRounds}"
        playerLabel.text = "♠ ${player.name} ♠"
        actionsLabel.text = "Aktionen: ${player.actionsLeft}"
        drawCount.text = "Karten: ${game.drawStack.size}"
    }
    /** update pick info text to show what is selected */
    private fun updatePickInfo() {
        val o = if (pickedOpenIndex >= 0) "Offen: ${pickedOpenIndex + 1}" else "Die Karte in der Hand: -"
        val c = if (pickedCenterIndex >= 0) "Tisch: ${pickedCenterIndex + 1}" else "Die Karte in der Mitte: -"
        pickInfo.text = "$o | $c"
    }
    /** update log rows with last messages */
    private fun updateLog() {
        for (i in logRows.indices) {
            if (i < logMessages.size) {
                logRows[i].text = logMessages[logMessages.size - 1 - i]
            } else {
                logRows[i].text = ""
            }
        }
    }
    /** refresh everything for current player */
    private fun showCurrentPlayer() {
        val game = rootService.currentGame ?: return
        resetPicks()
        showHand(game)
        showCenter(game)
        showOthers(game)
        updateDiscard(game)
        updateInfo(game)
    }
    /** called when game start, show first player cards */
    override fun refreshAfterStartNewGame() {
        val game = rootService.currentGame ?: return
        logMessages.clear()
        logOffset = 0
        resetPicks()
        showHand(game)
        showCenter(game)
        showOthers(game)
        updateDiscard(game)
        updateInfo(game)
        updateLog()
    }
    /** called when turn end, just update info */
    override fun refreshAfterTurnEnd() {
        val game = rootService.currentGame ?: return
        resetPicks()
        updateInfo(game)
    }
    /** called after card swap, refresh cards*/
    override fun refreshAfterSwitch() {
        val game = rootService.currentGame ?: return
        lock()
        resetPicks()
        showHand(game)
        showCenter(game)
        updateDiscard(game)
        updateInfo(game)
        playAnimation(DelayAnimation(500).apply { onFinished = { unlock() } })
    }
    /** called after push left, refresh center cards*/
    override fun refreshAfterPushLeft(newCard: Card) {
        val game = rootService.currentGame ?: return
        lock()
        resetPicks()
        showCenter(game)
        showHand(game)
        updateDiscard(game)
        updateInfo(game)
        playAnimation(DelayAnimation(500).apply { onFinished = { unlock() } })
    }
    /** called after push right, refresh center cards */
    override fun refreshAfterPushRight(newCard: Card) {
        val game = rootService.currentGame ?: return
        lock()
        resetPicks()
        showCenter(game)
        showHand(game)
        updateDiscard(game)
        updateInfo(game)
        playAnimation(DelayAnimation(500).apply { onFinished = { unlock() } })
    }
    /** called when error happen, add to log */
    override fun refreshAfterError(message: String) {
        logMessages.add("⚠ $message")
        updateLog()
    }
    /** called when new log message come */
    override fun refreshLog(message: String) {
        logMessages.add(message)
        updateLog()
    }
    /** call function showCurrentPlayer() */
    override fun refreshAfterStartTurn() {
        showCurrentPlayer()
    }
}