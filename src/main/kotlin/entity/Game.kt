package entity

import tools.aqua.bgw.util.Stack

data class Game(
    val nRounds: Int,
    val players: MutableList<Player> = mutableListOf(),
    val drawStack: Stack<Card> = Stack(),
    val playStack: Stack<Card> = Stack(),
    val centerCards: MutableList<Card> = mutableListOf(),
    var curPlayerIdx: Int = 0,
    val log: MutableList<String> = mutableListOf()
)