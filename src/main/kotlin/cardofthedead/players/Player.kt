package cardofthedead.players

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.cards.Zombie
import cardofthedead.decks.Deck
import cardofthedead.decks.getMovementPoints
import cardofthedead.decks.getZombiesAroundCount
import cardofthedead.game.Game
import cardofthedead.game.EventsFacade
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.random.Random

abstract class Player(
    val game: Game,
    val name: String,
    val sex: Sex
) {

    private val events: PublishSubject<EventsFacade.Event> = PublishSubject.create()

    internal val hand: Deck<Card> = Deck(game)

    /**
     * Temporary cards that may transition to hand eventually.
     * For certain actions.
     */
    internal val candidatesToHand: Deck<Card> = Deck(game)

    /**
     * Zombies that chase a player.
     * For this round.
     */
    internal val zombiesAround: Deck<Zombie> = Deck(game)

    /**
     * Action cards if gathered enough let player to escape from a round.
     * For this round.
     */
    internal val escapeCards: Deck<Action> = Deck(game)

    /**
     * Sum of all movement points from all escape cards played today for the player.
     * For the whole game.
     */
    private var survivalPoints: Int = 0

    /**
     * Amount of draws for player for a turn.
     */
    protected var drawCardThisTurn: Boolean = true

    // Decisions

    /**
     * Chooses N cards from the candidates deck.
     */
    abstract fun chooseSinglePointCards(n: Int)

    abstract fun decideToPlayCardFromHand(): PlayCardDecision

    abstract fun chooseWorstCandidateForBarricade(): Card?

    abstract fun chooseWorstMovementCardForDynamite(): Action?

    abstract fun decideToDrawNoCardsNextTurnForHide(): Boolean

    abstract fun choosePlayerToGiveZombieToForLure(): Player

    abstract fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean

    abstract fun choosePlayerToTakeCardFromForSlugger(): Player

    abstract fun choosePlayerToDiscardMovementCardsFromForTripped(): Player

    abstract fun decideHowManyMovementCardsToDiscardForTripped(): Int

    // Common logic

    fun getEvents(): Observable<EventsFacade.Event> = events

    fun publishEvent(event: EventsFacade.Event) = events.onNext(event)

    /**
     * Picks N top cards from the play deck to the candidates deck.
     */
    fun pickCandidateCards(n: Int) {
        repeat(n) {
            game.playDeck.pickTopCard()?.let(candidatesToHand::addCard)
        }
    }

    fun drawTopCard(): Card? =
        if (drawCardThisTurn) {
            game.playDeck.pickTopCard()
        } else {
            drawCardThisTurn = true
            null
        }

    /**
     * Do not combine with chasedByZombie cause they have different semantics.
     */
    fun takeToHand(card: Card) = hand.addCard(card)

    fun putOnBottom(card: Card) = game.playDeck.addCardOnBottom(card)

    fun play(card: Card) = card.play(this)

    fun chasedByZombie(zombie: Zombie) {
        zombiesAround.addCard(zombie)
    }

    /**
     * Add action card as an escape card.
     */
    fun addMovementPoints(actionCard: Action) {
        escapeCards.addCard(actionCard)
    }

    /**
     * Calculates sum of movement points for each escape card.
     */
    fun getMovementPoints(): Int = escapeCards.getMovementPoints()

    fun addSurvivalPoints(points: Int) {
        survivalPoints += points
    }

    fun getSurvivalPoints(): Int = survivalPoints

    fun getZombiesAroundCount(): Int = zombiesAround.getZombiesAroundCount()

    /**
     * Put a card, not belonging to any deck, to a discard deck.
     */
    fun discard(card: Card) = game.discardDeck.addCard(card)

    fun discardHand() {
        game.discardDeck.merge(hand)
    }

    fun discardCandidatesCards() {
        game.discardDeck.merge(candidatesToHand)
    }

    fun discardZombiesAround() {
        game.discardDeck.merge(zombiesAround)
    }

    fun discardEscapeCards() {
        game.discardDeck.merge(escapeCards)
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
    }

    fun throwCoin(): Boolean = Random.nextBoolean()
    fun throwDice(n: Int): Int = Random.nextInt(1, n + 1)

    override fun toString(): String {
        return "$name: " +
                "hnd=$hand, " +
                "cnddts=$candidatesToHand, " +
                "zmbs=$zombiesAround (${getZombiesAroundCount()}), " +
                "escp=$escapeCards (${getMovementPoints()}), " +
                "srvvl=$survivalPoints"
    }
}

data class PlayerDescriptor(
    val name: String,
    val level: Level = Level.EASY,
    val sex: Sex = Sex.MALE
)

enum class Level { EASY, HARD }
enum class Sex { MALE, FEMALE }

fun Player.getPronoun(): String = if (Sex.MALE == this.sex) "he" else "she"
