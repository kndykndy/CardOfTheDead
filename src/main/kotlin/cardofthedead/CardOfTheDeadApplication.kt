package cardofthedead

import cardofthedead.cards.StandardDeck
import cardofthedead.game.Game
import cardofthedead.players.Player

fun main() {
    Game.Builder(Player.of("Dimitry"), Player.of("Eugene"), StandardDeck())
        .withPlayer(Player.of("Stacey"))
        .build()
        .play()
}
