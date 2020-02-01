package main.kotlin.cardofthedead.players

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.cards.WayToPlayCard
import main.kotlin.cardofthedead.cards.Zombie

abstract class Player(
    name: String,
    level: Level
) {

    val hand: Deck = Deck()

    /**
     * Temporary cards deck.
     */
    val candidatesToHand: Deck = Deck()

    /**
     * Picks N top cards from the play deck to the candidates deck.
     */
    fun pickCards(playDeck: Deck, n: Int) {
        repeat(n) {
            playDeck.pickTopCard()?.let {
                candidatesToHand.addCard(it)
            }
        }
    }

    /**
     * Chooses N cards from the candidates deck.
     */
    abstract fun chooseSinglePointCards(n: Int)

    fun drawTopCard(playDeck: Deck): Card? = playDeck.pickTopCard()

    fun takeToHand(card: Card) = hand.addCard(card)

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

    fun addSurvivalPoints(pointsCount: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        fun of(name: String, level: Level = Level.EASY): Player {
            if (level == Level.EASY) {
                return EasyPlayer(name)
            } else {
                return EasyPlayer(name) // todo add other difficulties
            }
        }
    }
}

enum class Level {
    EASY,
    MEDIUM,
    HARD
}
