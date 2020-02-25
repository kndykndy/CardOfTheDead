package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.players.Player

class Pillage : Action(2) {

    /**
     * Blindly draw a card from every other player.
     */
    override fun play(player: Player) {
        player.gameContext.players
            .filterNot { it == player }
            .forEach { itPlayer ->
                player.hand.addCard(itPlayer.hand.pickRandomCard()!!)
            }
    }
}
