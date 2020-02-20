package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.players.Player

class Tripped : Action(1) {

    /**
     * Discard up to two of another player's newest movement cards.
     */
    override fun play(player: Player) {
        val playerToDiscardMovingCardsFrom =
            player.choosePlayerToDiscardMovementCardsFromForTripped()

        repeat(player.decideHowManyMovementCardsToDiscardForTripped()) {
            playerToDiscardMovingCardsFrom.discard(
                playerToDiscardMovingCardsFrom.escapeCards.pickTopCard()!!
            )
        }
    }
}
