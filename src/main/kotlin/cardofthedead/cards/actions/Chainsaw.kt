package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.getSingleZombieCards
import main.kotlin.cardofthedead.cards.getZombieCard
import main.kotlin.cardofthedead.cards.zombies.Zombies
import main.kotlin.cardofthedead.players.Player

class Chainsaw : Action(1) {

    override fun play(player: Player) {
        val zombiesAround = player.getZombieCards()
        if (zombiesAround.isNotEmpty()) {
            zombiesAround.getZombieCard(Zombies::class)?.let {
                player.discard(it)
                return
            }

            zombiesAround.getSingleZombieCards()
                .takeLast(2)
                .forEach { player.discard(it) }
        }
    }
}
