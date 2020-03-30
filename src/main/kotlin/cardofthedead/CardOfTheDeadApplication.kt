package cardofthedead

import cardofthedead.game.Game
import cardofthedead.players.Level
import cardofthedead.players.PlayerDescriptor
import cardofthedead.players.Sex

fun main() {
    Game.Builder(
        PlayerDescriptor("Luke"),
        PlayerDescriptor("Yoda")
    )
        .withPlayer(PlayerDescriptor("Rei", Level.EASY, Sex.FEMALE))
        .build()
        .play()
}
