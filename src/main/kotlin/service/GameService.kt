package service
import entity.Card
import entity.CardSuit
import entity.CardValue
import entity.Player
import entity.ScoreTable
import entity.Game
/**
 * this class control main rules of the game
 * @param rootService use other services and game state
 */
class GameService(private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * start new game
     * mix the players so the seating is random
     * everyone get 5 cards (2 hidden, 3 open) and 3 cards go to the center
     * @param playersNames list of player names
     * @param totalRounds how many rounds to play
     * @throws IllegalStateException if game is already running
     * @throws IllegalArgumentException if player count or round count is wrong or if name is empty
     */
    fun startNewGame(playersNames: MutableList<String>, totalRounds: Int) {
        check(rootService.currentGame == null) { "es gibt ein spiel " }
        require(playersNames.size in 2..4) { "die anzahl von spieler muss zwischen 2 und 4 sein" }
        require(totalRounds in 2..7) { "die anzahl von rounds muss zwischen 2 und 7 sein" }
        require(playersNames.all { it.isNotBlank() }) { "spielernamen müssen eingegeben werden" }

        val players = mutableListOf<Player>()
        for (name in playersNames) {
            val newPlayer = Player(name)
            players.add(newPlayer)
        }
        players.shuffle()

        val game = Game(totalRounds = totalRounds, players = players)
        rootService.currentGame = game
        createDrawStack()
        players.forEach { player ->
            repeat(2) { player.hiddenCards.add(game.drawStack.pop()) }
            repeat(3) { player.openCards.add(game.drawStack.pop()) }
        }
        repeat(3) { game.centerCards.add(game.drawStack.pop()) }

        game.currentPlayerIndex = 0
        updateLog("Spiel hat begonnen und Startspieler ${players[0].name}")
        onAllRefreshables { refreshAfterStartNewGame() }
    }
    /**
     * make 52 cards put them in the draw stack and shuffle them
     * @throws IllegalStateException if there is no game running
     */
    private fun createDrawStack() {
        val game = rootService.currentGame
        checkNotNull(game) { "aktuell läuft kein spiel" }
        for (suit in CardSuit.entries) {
            for (value in CardValue.entries) {
                val newCard = Card(suit, value)
                game.drawStack.push(newCard)
            }
        }
        game.drawStack.shuffle()
    }
    /**
     * take all cards from the discard stack and put them to the draw stack
     * only work if the draw stack is empty
     * @throws IllegalStateException if there is no game running or both stacks are empty
     */
    fun refillDrawStack() {
        val game = rootService.currentGame
        checkNotNull(game) { "es gibt kein spiel " }
        if (game.drawStack.isEmpty()) {
            if(game.discardStack.isEmpty()){
                onAllRefreshables { refreshAfterError("draw and discard stack are both empty") }
                return
            }
            val newDrawStackCards = game.discardStack.popAll()
            game.drawStack.pushAll(newDrawStackCards)
            game.drawStack.shuffle()
        }
    }
    /**
     * look at the player's 5 cards and calculate their poker score
     * @param player the player we want to check
     * @return the number value of score
     * @throws IllegalStateException if there is no game running
     * @throws IllegalArgumentException if the player does not have exactly 5 cards
     */
    fun evaluateCards(player: Player){
        checkNotNull(rootService.currentGame) { "es gibt kein spiel " }
        val cardsInHand = player.openCards + player.hiddenCards
        require(cardsInHand.size == 5) { "in der hand muss es genau 5 karte sein" }
        val values = mutableListOf<Int>()
        for (card in cardsInHand) {
            values.add(card.value.ordinal)
        }
        values.sort()
        val suits = mutableListOf<CardSuit>()
        for (card in cardsInHand) {
            suits.add(card.suit)
        }
        val counts = mutableListOf<Int>()
        for (value in values.toSet()) {
            var count = 0
            for (v in values) {
                if (v == value) count++
            }
            counts.add(count)
        }
        counts.sortDescending()
        val flush = suits.toSet().size == 1
        val aceLowStraight = values == listOf(0, 1, 2, 3, 12)
        val straight = (values.last() - values.first() == 4 && counts.size == 5) || aceLowStraight
        val score = when {
            flush && straight && values.first() == CardValue.TEN.ordinal -> ScoreTable.ROYALFLUSH
            flush && straight -> ScoreTable.STRAIGHTFLUSH
            counts[0] == 4 -> ScoreTable.FOUROFAKIND
            counts[0] == 3 && counts[1] == 2 -> ScoreTable.FULLHOUSE
            flush -> ScoreTable.FLUSH
            straight -> ScoreTable.STRAIGHT
            counts[0] == 3 -> ScoreTable.SET
            counts[0] == 2 && counts[1] == 2 -> ScoreTable.TWOPAIR
            counts[0] == 2 -> ScoreTable.PAIR
            else
                -> ScoreTable.HIGHCARD
        }
        player.score = score
    }
    /**
     * save message to the log and update the screen
     * @param message the text to show
     * @throws IllegalStateException if there is no game running
     */
    fun updateLog(message: String) {
        val game = rootService.currentGame
        checkNotNull(game) { "aktuell läuft kein spiel" }
        game.log.add(message)
        onAllRefreshables { refreshLog(message) }
    }
    /**
     * end the game
     * check everyone's points, make a ranking list and stop the game
     * @throws IllegalStateException if there is no game running
     */
    fun endGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "es gibt kein spiel am laufen" }
        for (player in game.players) {
            evaluateCards(player)
        }
        val ranking = game.players.sortedByDescending { player -> player.score.ordinal }
        updateLog("das spiel ist zu ende")
        rootService.currentGame = null
        onAllRefreshables { refreshAfterGameEnd(ranking) }
    }
    /**
     * give the turn to the next player
     * if everyone played,  new round start
     * if the last round is done, the game end
     * @throws IllegalStateException if there is no game running
     */
    fun endTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "es gibt kein spiel " }
        game.currentPlayerIndex = (game.currentPlayerIndex + 1) % game.players.size
        game.players[game.currentPlayerIndex].actionsLeft = 2
        if (game.currentPlayerIndex == 0) {
            game.currentRound++
            if (game.currentRound == game.totalRounds + 1) {
                endGame()
                return
            }
        }
        updateLog("Spieler ${game.players[game.currentPlayerIndex].name} kann jetzt spielen")
        onAllRefreshables { refreshAfterTurnEnd() }
    }
    fun startTurn(){
        val game = rootService.currentGame
        checkNotNull(game) { "es gibt kein spiel" }
        onAllRefreshables { refreshAfterStartTurn() }
    }
}