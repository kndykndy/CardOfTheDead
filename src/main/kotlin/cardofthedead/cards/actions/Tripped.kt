package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedTripped
import cardofthedead.game.Game
import cardofthedead.players.Player

class Tripped(
    game: Game
) : Action(
    game,
    "Tripped",
    "discard up to two of another player's newest movement cards"
) {

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
            PlayedTripped(playedBy, discardedMovementCards, playerToDiscardMovementCardsFrom)
        )
    }
}
