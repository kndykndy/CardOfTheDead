package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.Game
import cardofthedead.players.Player

class Pillage(gameContext: Game) : Action(gameContext, 2) {

    /**
     * Blindly draw a card from every other player.
     */
    override fun play(playedBy: Player) {
        gameContext.players
            .filterNot { it == playedBy }
            .forEach { it.hand.pickRandomCard()?.let(playedBy::takeToHand) }
    }
}
