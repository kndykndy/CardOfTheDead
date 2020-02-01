package main.kotlin.cardofthedead.players

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.cards.PlayCardDecision
import main.kotlin.cardofthedead.cards.Zombie

abstract class Player(
    val name: String,
    val level: Level
) {

    protected val hand: Deck = Deck()

    /**
     * Temporary cards deck.
     */
    protected val candidatesToHand: Deck = Deck()

    /**
     * Zombies that chase a player.
     */
    protected val zombiesAround: Deck = Deck()

    protected val escapeCards: Deck = Deck()

    protected var survivalPoints: Int = 0

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

    abstract fun decideToPlayCardFromHand(): PlayCardDecision

    fun drawTopCard(playDeck: Deck): Card? = playDeck.pickTopCard()

    fun takeToHand(card: Card) = hand.addCard(card)

    fun chasedByZombie(zombieCard: Zombie) = zombiesAround.addCard(zombieCard)

    fun play(card: Card) = card.play(this) // todo probably pass more context

    fun discard(card: Card, discardDeck: Deck) = discardDeck.addCard(card)

    fun addMovementPoints(actionCard: Action) = escapeCards.addCard(actionCard)

    fun getMovementPointsCount(): Int = escapeCards.getMovementPointsSum()

    fun addSurvivalPoints(pointsCount: Int) {
        survivalPoints += pointsCount
    }

    fun getZombiesAroundCount(): Int = zombiesAround.size()

    fun discardAllCards(discardDeck: Deck) {
        discardDeck.merge(hand)
        discardDeck.merge(candidatesToHand)
        discardDeck.merge(zombiesAround)
        discardDeck.merge(escapeCards)
    }

    fun discardCandidatesCards(discardDeck: Deck) {
        discardDeck.merge(candidatesToHand)
    }

    /**
     * Survival points are not touched.
     */
    fun die(discardDeck: Deck) {
        discardAllCards(discardDeck)
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
