package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.players.Player

class Bitten : Action(0) {

    /**
     * This card cannot be played as Movement card.
     */
    override fun play(player: Player, playDeck: Deck) {}
}
