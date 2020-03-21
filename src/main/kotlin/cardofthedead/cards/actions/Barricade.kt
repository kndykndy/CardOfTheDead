package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.players.Player

class Barricade : Action(1) {

    /**
     * Draw three cards. Choose one and put it on the bottom of the deck.
     */
    override fun play(playedBy: Player) {
        playedBy.pickCards(3)

        playedBy.chooseWorstCandidateForBarricade()
            ?.let(gameContext.playDeck::addCardOnBottom)

        repeat(2) {
            playedBy.takeTopCandidateToHand()
        }
    }
}
