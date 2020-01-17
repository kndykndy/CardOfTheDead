package main.kotlin.cardofthedead.players

import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.Deck

class Player(
    name: String
) {
    val hand: Set<Card>
        get() {
            TODO()
        }

    val theRestOfHand: Set<Card>
        get() {
            TODO()
        }

    fun pickCards(num: Deck, deck: Int): Set<Card> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun chooseSinglePointCards(i1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun returnCardsToDeck(theRestOfCards: Set<Card>, deck: Deck) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun drawTopCard(deck: Deck): Card {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun playLastCard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun playCardFromHand() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
