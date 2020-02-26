package cardofthedead

import cardofthedead.game.Game
import cardofthedead.players.Player

fun main() {
    Game.Builder()
        .withPlayer(Player.of("Dimitry"))
        .withPlayer(Player.of("Eugene"))
        .withPlayer(Player.of("Stacey"))
        .build()
        .play()
}
