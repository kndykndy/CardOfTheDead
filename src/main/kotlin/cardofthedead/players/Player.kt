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

    protected lateinit var gameContext: Game

    protected val hand: Deck = Deck()

    /**
     * Temporary cards deck.
     */
    protected val candidatesToHand: Deck = Deck()

    /**
     * Zombies that chase a player.
     */
    protected val zombiesAround: Deck = Deck()

    private val escapeCards: Deck = Deck()

    private var survivalPoints: Int = 0

    // Decision funcs

    /**
     * Chooses N cards from the candidates deck.
     */
    abstract fun chooseSinglePointCards(n: Int)

    abstract fun decideToPlayCardFromHand(): PlayCardDecision

    fun getCardOfClass(card: KClass<out Card>): Card? = hand.getCardOfClass(card)

    fun getCardsOfClass(card: KClass<out Card>): List<Card> = hand.getCardsOfClass(card)

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

    fun drawTopCard(playDeck: Deck): Card? = playDeck.pickTopCard()

    fun takeToHand(card: Card) = hand.addCard(card)

    fun takeTopCandidateToHand() = hand.addCard(candidatesToHand.pickTopCard())

    fun putOnBottom(card: Card, deck: Deck) = deck.addCardOnBottom(card)

    fun play(card: Card, playDeck: Deck) = card.play(this, playDeck)

    fun chasedByZombie(zombieCard: Zombie) = zombiesAround.addCard(zombieCard)

    fun addMovementPoints(actionCard: Action) = escapeCards.addCard(actionCard)

    fun getMovementPointsCount(): Int = escapeCards.getMovementPointsSum()

    fun addSurvivalPoints(pointsCount: Int) {
        survivalPoints += pointsCount
    }

    fun getZombiesAroundCount(): Int = zombiesAround.size()

    fun discard(card: Card, discardDeck: Deck) = discardDeck.addCard(card)

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

    // Cards specific funcs

    abstract fun chooseWorstCandidateForBarricade(): Card?


    companion object {

        fun of(name: String, level: Level = Level.EASY): Player = EasyPlayer(name)

        fun setGameContext(player: Player, game: Game) {
            player.gameContext = game
        }
    }
}

enum class Level {
    EASY,
    MEDIUM,
    HARD
}
