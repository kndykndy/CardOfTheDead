package main.kotlin.cardofthedead.players

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Deck

class EasyPlayer(name: String) : Player(name, Level.EASY) {

    override fun pickCards(deck: Deck, n: Int) {
        repeat(n) {
            val topCard = deck.pickTopCard()
            if (topCard != null) {
                theRestOfHand.addCard(topCard)
            }
        }
    }

    /**
     * Picks random N cards from the rest of the hand.
     */
    override fun chooseSinglePointCards(n: Int) {
        if (theRestOfHand.size() > n) {
            theRestOfHand.pickActionCards()
            hand.addAll(
                theRestOfHand
                    .filterIsInstance<Action>()
                    .shuffled()
                    .take(n)
            )
        } else {

        }
    }

}