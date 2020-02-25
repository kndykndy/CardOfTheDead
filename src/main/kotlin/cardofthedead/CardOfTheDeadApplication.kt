package cardofthedead

import main.kotlin.cardofthedead.game.Game
import main.kotlin.cardofthedead.players.Level
import main.kotlin.cardofthedead.players.Player

fun main() {
    Game.Builder()
        .withPlayer(Player.of("Dimitry", Level.EASY))
        .withPlayer(Player.of("Eugene", Level.EASY))
        .withPlayer(Player.of("Stacey", Level.EASY))
        .build()
        .play()
}
