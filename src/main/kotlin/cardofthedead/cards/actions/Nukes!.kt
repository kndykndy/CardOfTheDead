package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.players.Player

@Suppress("ClassName")
class `Nukes!` : Action(2) {

    /**
     * Discard all zombie cards and all cards in hand from all players (including yourself).
     */
    override fun play(playedBy: Player) {
        gameContext.players.forEach { player ->
            player.discardZombiesAround()
            player.discardHand()
        }
    }
}
