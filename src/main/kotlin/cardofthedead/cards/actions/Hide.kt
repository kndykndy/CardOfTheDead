package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.getSingleZombies
import cardofthedead.players.Player

class Hide : Action(1) {

    /**
     * Give one of your zombie cards to the next player.
     * You may choose to draw no cards on your next turn.
     */
    override fun play(playedBy: Player) {
        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            zombiesAround.pickCard(
                zombiesAround.getSingleZombies().random()
            )?.let {
                gameContext
                    .getNextPlayer(playedBy)
                    .chasedByZombie(it)
            }
        }

        playedBy.decideToDrawNoCardsNextTurnForHide()
    }
}
