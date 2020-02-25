package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.getSingleZombies
import main.kotlin.cardofthedead.players.Player

class Lure : Action(1) {

    /**
     * Give one of your zombie cards to any player.
     */
    override fun play(player: Player) {
        val zombiesAround = player.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            zombiesAround.pickCard(
                zombiesAround.getSingleZombies().random()
            )?.let {
                player.choosePlayerToGiveZombieToForLure()
                    .chasedByZombie(it)
            }
        }
    }
}
