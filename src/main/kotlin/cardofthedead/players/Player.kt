package cardofthedead.players

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.cards.Zombie
import cardofthedead.decks.Deck
import cardofthedead.decks.getMovementPoints
import cardofthedead.decks.getZombiesAroundCount
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.random.Random

abstract class Player(
    val gameContext: Game,
    val name: String,
    val sex: Sex
) {

    internal val events: PublishSubject<MessagesFacade.Message> = PublishSubject.create()

    internal val hand: Deck<Card> = Deck(gameContext)

    /**
     * Temporary cards that may transition to hand eventually.
     * For certain actions.
     */
    internal val candidatesToHand: Deck<Card> = Deck(gameContext)

    /**
     * Zombies that chase a player.
     * For this round.
     */
    internal val zombiesAround: Deck<Zombie> = Deck(gameContext)

    /**
     * Action cards if gathered enough let player to escape from a round.
     * For this round.
     */
    internal val escapeCards: Deck<Action> = Deck(gameContext)

    /**
     * Sum of all movement points from all escape cards played today for the player.
     * For the whole game.
     */
    private var survivalPoints: Int = 0

    /**
     * Amount of draws for player for a turn.
     */
    protected var doDrawCardThisTurn: Boolean = true

    // Decision funcs

    /**
     * Chooses N cards from the candidates deck.
     */
    abstract fun chooseSinglePointCards(n: Int)

    abstract fun decideToPlayCardFromHand(): PlayCardDecision

    abstract fun chooseWorstCandidateForBarricade(): Card?

    abstract fun chooseWorstMovementCardForDynamite(): Action?

    abstract fun decideToDrawNoCardsNextTurnForHide() : Boolean

    abstract fun choosePlayerToGiveZombieToForLure(): Player

    abstract fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean

    abstract fun choosePlayerToTakeCardFromForSlugger(): Player

    abstract fun choosePlayerToDiscardMovementCardsFromForTripped(): Player

    abstract fun decideHowManyMovementCardsToDiscardForTripped(): Int

    // Common logic

    /**
     * Picks N top cards from the play deck to the candidates deck.
     */
    fun pickCandidateCards(n: Int) {
        repeat(n) {
            gameContext.playDeck.pickTopCard()?.let(candidatesToHand::addCard)
        }
    }

    fun drawTopCard(): Card? =
        if (doDrawCardThisTurn) {
            gameContext.playDeck.pickTopCard()
        } else {
            doDrawCardThisTurn = true
            null
        }

    /**
     * Do not combine with chasedByZombie cause they have different semantics.
     */
    fun takeToHand(card: Card) = hand.addCard(card)

    fun putOnBottom(card: Card) = gameContext.playDeck.addCardOnBottom(card)

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
