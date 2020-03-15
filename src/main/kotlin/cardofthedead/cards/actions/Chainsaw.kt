package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.getSingleZombies
import cardofthedead.cards.zombies.Zombies
import cardofthedead.players.Player

class Chainsaw : Action(1) {

    /**
     * Discard two zombie cards.
     */
    override fun play(playedBy: Player) {
        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            zombiesAround.pickCardOfClass(Zombies::class.java)?.let {
                playedBy.discard(it)
                return
            }

            zombiesAround.getSingleZombies()
                .takeLast(2)
                .forEach { playedBy.discard(zombiesAround.pickCard(it)!!) }
        }
    }
}
