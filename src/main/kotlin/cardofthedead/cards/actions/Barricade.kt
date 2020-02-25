package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.players.Player

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
