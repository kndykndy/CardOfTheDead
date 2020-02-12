package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.players.Player

class Armored : Action(1) {

    override fun play(player: Player, playDeck: Deck) {
        player.getCardOfClass(Bitten::class)?.let {
            player.putOnBottom(it, playDeck)
        }
    }
}
