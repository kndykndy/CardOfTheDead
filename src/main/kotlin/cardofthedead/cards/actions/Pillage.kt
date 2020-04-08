package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

class Pillage(gameContext: Game) : Action(gameContext, 2) {

    /**
     * Blindly draw a card from every other player.
     */
    override fun play(playedBy: Player) {
        val pillagedCards = mutableListOf<Action>()

        gameContext.players
            .filterNot { it == playedBy }
            .forEach {
                it.hand
                    .pickRandomCard()
                    ?.let { card ->
                        playedBy.takeToHand(card)
                        pillagedCards.add(card as Action)
                    }
            }

        playedBy.events.onNext(
            MessagesFacade.Game.ActionCards.PlayedPillage(playedBy, pillagedCards)
        )
    }
}
