package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

class Tripped(game: Game) : Action(game, 1) {

    /**
     * Discard up to two of another player's newest movement cards.
     */
    override fun play(playedBy: Player) {
        val playerToDiscardMovementCardsFrom =
            playedBy.choosePlayerToDiscardMovementCardsFromForTripped()

        val discardedMovementCards = mutableListOf<Action>()

        repeat(playedBy.decideHowManyMovementCardsToDiscardForTripped()) {
            playerToDiscardMovementCardsFrom
                .escapeCards
                .pickTopCard()
                ?.let {
                    playerToDiscardMovementCardsFrom.discard(it)
                    discardedMovementCards.add(it)
                }
        }

        playedBy.publishEvent(
            MessagesFacade.Game.ActionCards.PlayedTripped(
                playedBy, discardedMovementCards, playerToDiscardMovementCardsFrom
            )
        )
    }
}
