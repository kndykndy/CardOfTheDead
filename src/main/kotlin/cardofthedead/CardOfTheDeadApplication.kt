package cardofthedead

import cardofthedead.game.Game
import cardofthedead.players.Level
import cardofthedead.players.Player

fun main() {
    Game.Builder()
        .withPlayer(Player.of("Dimitry", Level.EASY))
        .withPlayer(Player.of("Eugene", Level.EASY))
        .withPlayer(Player.of("Stacey", Level.EASY))
        .build()
        .play()
}
