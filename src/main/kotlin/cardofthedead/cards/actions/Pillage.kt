package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.players.Player

class Pillage : Action(2) {

    /**
     * Blindly draw a card from every other player.
     */
    override fun play(player: Player) {
        player.gameContext.players
            .filterNot { it == player }
            .forEach { cPlayer ->
                player.hand.addCard(cPlayer.hand.pickRandomCard()!!)
            }
    }
}
