package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.players.Player

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
