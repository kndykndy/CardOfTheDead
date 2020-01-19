package main.kotlin.cardofthedead

import main.kotlin.cardofthedead.game.Game
import main.kotlin.cardofthedead.players.Level
import main.kotlin.cardofthedead.players.Player

fun main() {
    val game = Game.Builder()
        .withPlayer(Player("Дима", Level.EASY))
        .withPlayer(Player("Женя", Level.MEDIUM))
        .withPlayer(Player("Настя", Level.HIGH))
        .build()
    game.play()
}
