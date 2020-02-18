package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.zombies.Zombies
import main.kotlin.cardofthedead.players.Player

class Chainsaw : Action(1) {

    override fun play(player: Player) {
        val zombiesAround = player.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            zombiesAround.pickCardOfClass(Zombies::class)?.let {
                player.discard(it)
                return
            }

            zombiesAround.getSingleZombieCards()
                .takeLast(2)
                .forEach { player.discard(it) }
        }
    }
}
