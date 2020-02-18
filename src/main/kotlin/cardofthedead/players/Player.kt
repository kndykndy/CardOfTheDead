package main.kotlin.cardofthedead.players

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.cards.PlayCardDecision
import main.kotlin.cardofthedead.cards.Zombie
import main.kotlin.cardofthedead.game.Game
import kotlin.reflect.KClass

abstract class Player(
    val name: String
) {

    internal lateinit var gameContext: Game

    protected val hand: Deck<Card> = Deck()

    /**
     * Temporary cards deck.
     */
    protected val candidatesToHand: Deck<Card> = Deck()

    /**
     * Zombies that chase a player.
     */
    protected val zombiesAround: Deck<Zombie> = Deck()

    protected val escapeCards: Deck<Action> = Deck()

    private var survivalPoints: Int = 0

    // Decision funcs

    /**
     * Chooses N cards from the candidates deck.
     */
    abstract fun chooseSinglePointCards(n: Int)

    abstract fun decideToPlayCardFromHand(): PlayCardDecision

    abstract fun chooseWorstCandidateForBarricade(): Card?

    abstract fun chooseWorstMovementCardForDynamite(): Card?

    // Common logic

    fun getCardOfClass(card: KClass<out Card>): Card? = hand.getCardOfClass(card)

    fun getCardsOfClass(card: KClass<out Card>): List<Card> = hand.getCardsOfClass(card)

    fun getZombieCards(): List<Zombie> = zombiesAround.getInnerCards()

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

    fun drawTopCard(): Card? = gameContext.playDeck.pickTopCard()

    fun takeToHand(card: Card) = hand.addCard(card)

    fun takeTopCandidateToHand() = hand.addCard(candidatesToHand.pickTopCard())

    fun putOnBottom(card: Card, deck: Deck<Card>) = deck.addCardOnBottom(card)

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

        fun of(name: String, level: Level = Level.EASY): Player = EasyPlayer(name)
    }
}

enum class Level {
    EASY,
    MEDIUM,
    HARD
}
