package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.getSingleZombies
import cardofthedead.players.Player

class Hide : Action(1) {

    /**
     * Give one of your zombie cards to the next player.
     * You may choose to draw no cards on your next turn.
     */
    override fun play(player: Player) {
        val zombiesAround = player.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            zombiesAround.pickCard(
                zombiesAround.getSingleZombies().random()
            )?.let {
                player.gameContext
                    .getNextPlayer(player)
                    .chasedByZombie(it)
            }
        }

        player.decideToDrawNoCardsNextTurnForHide()
    }
}
