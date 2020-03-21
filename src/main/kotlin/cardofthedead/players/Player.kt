package cardofthedead.players

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.Deck
import cardofthedead.cards.PlayCardDecision
import cardofthedead.cards.Zombie
import cardofthedead.cards.getMovementPoints
import cardofthedead.game.Game
import kotlin.random.Random

abstract class Player(
    val name: String
) {

    internal lateinit var gameContext: Game

    internal val hand: Deck<Card> = Deck()

    /**
     * Temporary cards deck.
     */
    internal val candidatesToHand: Deck<Card> = Deck()

    /**
     * Zombies that chase a player.
     */
    internal val zombiesAround: Deck<Zombie> = Deck()

    internal val escapeCards: Deck<Action> = Deck()

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

    abstract fun choosePlayerToDiscardMovementCardsFromForTripped(): Player

    abstract fun decideHowManyMovementCardsToDiscardForTripped(): Int

    // Common logic

    /**
     * Picks N top cards from the play deck to the candidates deck.
     */
    fun pickCards(n: Int) {
        repeat(n) {
            gameContext.playDeck.pickTopCard()?.let(candidatesToHand::addCard)
        }
    }

    fun drawTopCard(): Card? =
        if (doDrawCardThisTurn) {
            val drawnCard = gameContext.playDeck.pickTopCard()
            if (drawnCard == null) {
                println("The deck is empty. ${this.name} can't draw a card.")
            }
            drawnCard
        } else {
            println("${this.name} uses their chance not to draw a card this turn.")
            doDrawCardThisTurn = true
            null
        }

    /**
     * Do not combine with chasedByZombie cause they have different semantics.
     */
    fun takeToHand(card: Card) = hand.addCard(card)

    fun takeTopCandidateToHand() = candidatesToHand.pickTopCard()?.let(hand::addCard)

    fun putOnBottom(card: Card) = gameContext.playDeck.addCardOnBottom(card)

    fun play(card: Card) = card.play(this)

    fun chasedByZombie(zombieCard: Zombie) {
        zombiesAround.addCard(zombieCard)

        println("${this.name} is chased by ${getZombiesAroundCount()} zombies now.")
    }

    fun addMovementPoints(actionCard: Action) {
        escapeCards.addCard(actionCard)

        println("${this.name} has ${getMovementPointsCount()} movement points now.")
    }

    fun getMovementPointsCount(): Int = escapeCards.getMovementPoints()

    fun addSurvivalPoints(pointsCount: Int) {
        survivalPoints += pointsCount
    }

    fun getZombiesAroundCount(): Int = zombiesAround.cards.sumBy { it.zombiesOnCard }

    /**
     * Put a card, not belonging to any deck, to a discard deck.
     */
    fun discard(card: Card) = gameContext.discardDeck.addCard(card)

    fun discardHand() {
        gameContext.discardDeck.merge(hand)
    }

    fun discardCandidatesCards() {
        gameContext.discardDeck.merge(candidatesToHand)
    }

    fun discardZombiesAround() {
        gameContext.discardDeck.merge(zombiesAround)
    }

    fun discardEscapeCards() {
        gameContext.discardDeck.merge(escapeCards)
    }

    fun discardAllCards() {
        discardHand()
        discardCandidatesCards()
        discardZombiesAround()
        discardEscapeCards()
    }

    /**
     * Survival points are not touched.
     */
    fun die() {
        discardAllCards()

        println("Oops... ${this.name} was eaten by zombies.")
    }

    companion object {

        fun of(name: String, level: Level? = Level.EASY): Player =
            if (level != null) {
                if (level == Level.HARD) HardPlayer(name) else EasyPlayer(name)
            } else {
                if (Random.nextBoolean()) HardPlayer(name) else EasyPlayer(name)
            }
    }
}

enum class Level { EASY, HARD }
