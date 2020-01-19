package main.kotlin.cardofthedead.players

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.cards.WayToPlayCard
import main.kotlin.cardofthedead.cards.Zombie

class Player(
    name: String,
    level: Level
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

    fun playCardFromHand(): WayToPlayCard {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun takeToHand(card: Card) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun chasedByZombie(card: Zombie) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun play(card: Card) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun discard(card: Card) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun decideToPlayCardFromHand(): WayToPlayCard {
        // todo if not surrounded

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun drawFromHand(): Action {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun addMovementPoints(actionCardFromHand: Action) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getZombiesAroundCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun die() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getMovementPointsCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

enum class Level {
    EASY,
    MEDIUM,
    HIGH
}
