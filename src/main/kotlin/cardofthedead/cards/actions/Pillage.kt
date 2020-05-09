package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedPillage
import cardofthedead.game.Game
import cardofthedead.players.Player

class Pillage(
    game: Game
) : Action(
    game,
    "Pillage",
    "blindly draw a card from every other player",
    2
) {

    /**
     * Blindly draw a card from every other player.
     */
    override fun play(playedBy: Player) {
        val pillagedCards = mutableListOf<Action>()

        game.players
            .filterNot { it == playedBy }
            .forEach {
                it.hand
                    .pickRandomCard()
                    ?.let { card ->
                        playedBy.takeToHand(card)
                        pillagedCards.add(card as Action)
                    }
            }

        playedBy.publishEvent(PlayedPillage(playedBy, pillagedCards))
    }
}
