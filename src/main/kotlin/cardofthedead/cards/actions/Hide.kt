package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.players.Player

class Hide : Action(1) {

    /**
     * Give one of your zombie cards to the next player.
     * You may choose to draw no cards on your next turn.
     */
    override fun play(player: Player) {
        val zombiesAround = player.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            zombiesAround.pickCard(
                zombiesAround.getSingleZombieCards().random()
            )?.let {
                player.gameContext
                    .getNextPlayer(player)
                    .chasedByZombie(it)
            }
        }

        player.decideToDrawNoCardsNextTurn()
    }
}
