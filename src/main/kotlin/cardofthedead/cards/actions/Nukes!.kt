package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.players.Player

@Suppress("ClassName")
class `Nukes!` : Action(2) {

    /**
     * Discard all zombie cards and all cards in hand from all players (including yourself).
     */
    override fun play(player: Player) {
        player.gameContext.players.forEach { itPlayer ->
            val playerZombies = itPlayer.zombiesAround
            playerZombies.cards.forEach {
                itPlayer.discard(playerZombies.pickCard(it)!!)
            }

            val playerHand = itPlayer.hand
            playerHand.cards.forEach {
                itPlayer.discard(playerHand.pickCard(it)!!)
            }
        }
    }
}
