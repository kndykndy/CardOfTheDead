package main.kotlin.cardofthedead.players

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.cards.PlayCardDecision
import main.kotlin.cardofthedead.cards.Zombie
import main.kotlin.cardofthedead.game.Game
import kotlin.random.Random

abstract class Player(
    val name: String
) {

    internal lateinit var gameContext: Game

    internal val hand: Deck<Card> = Deck()

    /**
     * Temporary cards deck.
     */
    protected val candidatesToHand: Deck<Card> = Deck()

    /**
     * Zombies that chase a player.
     */
    internal val zombiesAround: Deck<Zombie> = Deck()

    protected val escapeCards: Deck<Action> = Deck()

    private var survivalPoints: Int = 0

    protected var doDrawCardThisTurn: Boolean = true

    // Decision funcs

    /**
     * Chooses N cards from the candidates deck.
     */
    abstract fun chooseSinglePointCards(n: Int)

    abstract fun decideToPlayCardFromHand(): PlayCardDecision

    abstract fun chooseWorstCandidateForBarricade(): Card?

    abstract fun chooseWorstMovementCardForDynamite(): Card?

    abstract fun decideToDrawNoCardsNextTurnForHide()

    abstract fun choosePlayerToGiveZombieToForLure(): Player

    abstract fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean

    abstract fun choosePlayerToTakeCardFromForSlugger(): Player

    // Common logic

    /**
     * Picks N top cards from the play deck to the candidates deck.
     */
    fun pickCards(n: Int) {
        repeat(n) {
            gameContext.playDeck.pickTopCard()?.let {
                candidatesToHand.addCard(it)
            }
        }
    }

    fun drawTopCard(): Card? =
        if (doDrawCardThisTurn) {
            gameContext.playDeck.pickTopCard()
        } else {
            doDrawCardThisTurn = true
            null
        }

    // todo think over joining this func with chasedbyzombie
    fun takeToHand(card: Card) = hand.addCard(card)

    fun takeTopCandidateToHand() = candidatesToHand.pickTopCard()?.let { hand.addCard(it) }

    fun putOnBottom(card: Card) = gameContext.playDeck.addCardOnBottom(card)

    fun play(card: Card) = card.play(this)

    fun chasedByZombie(zombieCard: Zombie) = zombiesAround.addCard(zombieCard)

    fun addMovementPoints(actionCard: Action) = escapeCards.addCard(actionCard)

    fun getMovementPointsCount(): Int = escapeCards.getMovementPointsSum()

    fun addSurvivalPoints(pointsCount: Int) {
        survivalPoints += pointsCount
    }

    fun getZombiesAroundCount(): Int = zombiesAround.size()

    /**
     * Put a card, not belonging to any deck, to a discard deck.
     */
    fun discard(card: Card) = gameContext.discardDeck.addCard(card)

    fun discardAllCards() {
        gameContext.discardDeck.merge(hand)
        gameContext.discardDeck.merge(candidatesToHand)
        gameContext.discardDeck.merge(zombiesAround)
        gameContext.discardDeck.merge(escapeCards)
    }

    fun discardCandidatesCards() {
        gameContext.discardDeck.merge(candidatesToHand)
    }

    /**
     * Survival points are not touched.
     */
    fun die() {
        discardAllCards()
    }

    companion object {

        fun of(name: String, level: Level?): Player =
            if (level != null) {
                if (level == Level.HARD) HardPlayer(name) else EasyPlayer(name)
            } else {
                if (Random.nextBoolean()) HardPlayer(name) else EasyPlayer(name)
            }
    }
}

enum class Level { EASY, HARD }
