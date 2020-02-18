package main.kotlin.cardofthedead

import main.kotlin.cardofthedead.game.Game
import main.kotlin.cardofthedead.players.Level
import main.kotlin.cardofthedead.players.Player

fun main() {
    val game = Game.Builder()
        .withPlayer(Player.of("Дима", Level.EASY))
        .withPlayer(Player.of("Женя", Level.EASY))
        .withPlayer(Player.of("Настя", Level.HARD))
        .build()
    game.play()
}
