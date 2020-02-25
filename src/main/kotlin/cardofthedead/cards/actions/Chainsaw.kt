package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.getSingleZombies
import main.kotlin.cardofthedead.cards.zombies.Zombies
import main.kotlin.cardofthedead.players.Player

class Chainsaw : Action(1) {

    /**
     * Discard two zombie cards.
     */
    override fun play(player: Player) {
        val zombiesAround = player.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            zombiesAround.pickCardOfClass(Zombies::class.java)?.let {
                player.discard(it)
                return
            }

            zombiesAround.getSingleZombies()
                .takeLast(2)
                .forEach { player.discard(it) }
        }
    }
}
