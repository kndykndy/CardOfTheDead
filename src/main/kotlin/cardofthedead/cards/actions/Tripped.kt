package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.Game
import cardofthedead.players.Player

class Tripped(gameContext: Game) : Action(gameContext, 1) {

    /**
     * Discard up to two of another player's newest movement cards.
     */
    override fun play(playedBy: Player) {
        val playerToDiscardMovementCardsFrom =
            playedBy.choosePlayerToDiscardMovementCardsFromForTripped()

        repeat(playedBy.decideHowManyMovementCardsToDiscardForTripped()) {
            playerToDiscardMovementCardsFrom.escapeCards.pickTopCard()
                ?.let(playerToDiscardMovementCardsFrom::discard)
        }
    }
}
