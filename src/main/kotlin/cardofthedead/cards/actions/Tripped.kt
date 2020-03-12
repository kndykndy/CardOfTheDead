package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.players.Player

class Tripped : Action(1) {

    /**
     * Discard up to two of another player's newest movement cards.
     */
    override fun play(playedBy: Player) {
        val playerToDiscardMovingCardsFrom =
            playedBy.choosePlayerToDiscardMovementCardsFromForTripped()

        repeat(playedBy.decideHowManyMovementCardsToDiscardForTripped()) {
            playerToDiscardMovingCardsFrom.discard(
                playerToDiscardMovingCardsFrom.escapeCards.pickTopCard()!!
            )
        }
    }
}
