package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.players.Player

class Barricade : Action(1) {

    /**
     * Draw three cards. Choose one and put it on the bottom of the deck.
     */
    override fun play(player: Player) {
        player.pickCards(3)

        player.chooseWorstCandidateForBarricade()?.let {
            gameContext.playDeck.addCardOnBottom(it)
        }

        repeat(2) {
            player.takeTopCandidateToHand()
        }
    }
}
