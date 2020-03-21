package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.getSingleZombies
import cardofthedead.players.Player

class Lure : Action(1) {

    /**
     * Give one of your zombie cards to any player.
     */
    override fun play(playedBy: Player) {
        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            zombiesAround.pickCard(
                zombiesAround.getSingleZombies().random() // todo will fail if empty
            )?.let(playedBy.choosePlayerToGiveZombieToForLure()::chasedByZombie)
        }
    }
}
