package cardofthedead

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.cards.Zombie
import cardofthedead.decks.Deck
import cardofthedead.decks.EmptyDeck
import cardofthedead.decks.StandardDeck
import cardofthedead.game.EventsFacade
import cardofthedead.game.Game
import cardofthedead.players.EasyPlayer
import cardofthedead.players.HardPlayer
import cardofthedead.players.Player
import cardofthedead.players.PlayerDescriptor
import cardofthedead.players.Sex
import io.kotest.assertions.Failures
import io.mockk.spyk
import io.reactivex.rxjava3.observers.TestObserver
import kotlin.reflect.KClass

object TestUtils {

    // Game

    private fun gameWithDeck(
        playerDescriptor: PlayerDescriptor,
        deck: Deck<Card>
    ) = Game.Builder(
        PlayerDescriptor("Luke Skywalker"),
        PlayerDescriptor("Obi Van Kenobi"),
        deck
    )
        .withPlayer(playerDescriptor)
        .build()

    fun gameWithEmptyDeck() = gameWithDeck(PlayerDescriptor("C3PO"), EmptyDeck())
    fun gameWithStandardDeck() = gameWithDeck(PlayerDescriptor("C3PO"), StandardDeck())

    fun Game.wrapPlayersAsSpyKs(): Game {
        this.players.replaceAll { spyk(it) }
        return this
    }

    fun Game.getFirstPlayer() = this.players.first()

    fun Game.assertEvent(event: EventsFacade.Event) {
        val observer = this.getEventQueueTestObserver()
        observer.assertNoErrors()
        observer.assertValueCount(1)
        try {
            observer.assertValueAt(0) { it == event }
        } catch (a: AssertionError) {
            println("expected: $event")
            println("actual:   ${observer.values().getOrNull(0)}")
            throw a
        }
    }

    fun <T : EventsFacade.Event> TestObserver<EventsFacade.Event>.getValue(
        eventKlass: KClass<out T>
    ): T {

        val first = this.values().firstOrNull { eventKlass.isInstance(it) }
        @Suppress("UNCHECKED_CAST")
        if (first != null)
            return first as T
        else
            throw Failures.failure("Event with klass ${eventKlass.simpleName} has not occurred.")
    }

    fun <T : EventsFacade.Event> TestObserver<EventsFacade.Event>.getValues(
        eventKlass: KClass<out T>, count: Int, atLeast: Boolean = false
    ): List<T> {

        val list = this.values().filter { eventKlass.isInstance(it) }
        if (!list.isNullOrEmpty()) {
            @Suppress("UNCHECKED_CAST")
            if (list.size == count || (atLeast && list.size >= count))
                return list as List<T>
            else {
                val infix = if (atLeast) "at least " else ""
                throw Failures.failure(
                    "Expected $infix$count events of klass ${eventKlass.simpleName}, " +
                            "got ${list.size}."
                )
            }
        } else
            throw Failures.failure("Events with klass ${eventKlass.simpleName} have not occurred.")
    }

    fun <T : EventsFacade.Event> TestObserver<EventsFacade.Event>.getAnyOfValues(
        eventKlasses: List<KClass<out T>>, atLeastCount: Int
    ): List<T> {

        val list = this.values().filter { value ->
            eventKlasses.mapNotNull { if (it.isInstance(value)) true else null }.isNotEmpty()
        }
        if (!list.isNullOrEmpty()) {
            @Suppress("UNCHECKED_CAST")
            if (list.size >= atLeastCount)
                return list as List<T>
            else {
                throw Failures.failure(
                    "Expected at least $atLeastCount events of ${eventKlasses.size} klasses, " +
                            "got ${list.size}."
                )
            }
        } else
            throw Failures.failure("Events with given klasses have not occurred.")
    }

    // Players

    fun playerStub(game: Game): Player =
        object : Player(game, "R2D2", Sex.NONBINARY) {
            override fun chooseSinglePointCardsFromCandidates(n: Int) {}
            override fun decideToPlayCardFromHand() = PlayCardDecision.doNotPlay()
            override fun chooseWorstCandidateForBarricade(): Card? = null
            override fun chooseWorstMovementCardForDynamite(): Action? = null
            override fun decideToDrawNoCardsNextTurnForHide(): Boolean = false
            override fun choosePlayerToGiveZombieToForLure(): Player = this
            override fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean = false
            override fun choosePlayerToTakeCardFromForSlugger(): Player = this
            override fun choosePlayerToDiscardMovementCardsFromForTripped(): Player = this
            override fun decideHowManyMovementCardsToDiscardForTripped(): Int = 0
        }

    fun easyPlayerStub(game: Game): EasyPlayer = EasyPlayer(game, "Kylo Ren", Sex.FEMALE)
    fun hardPlayerStub(game: Game): HardPlayer = HardPlayer(game, "Darth Vader", Sex.MALE)

    fun Player.takeToHand(vararg listOfCards: Card) =
        listOfCards.forEach { this.takeToHand(it) }

    fun Player.takeToCandidates(vararg listOfCards: Card) =
        listOfCards.forEach { this.candidatesToHand.addCard(it) }

    fun Player.chasedByZombies(vararg listOfZombies: Zombie) =
        listOfZombies.forEach { this.chasedByZombie(it) }

    fun Player.addMovementPoints(vararg listOfActions: Action) =
        listOfActions.forEach { this.addMovementPoints(it) }

    // Cards

    fun <T : Card> Deck<T>.addCards(vararg listOfCards: T) =
        listOfCards.forEach { this.addCard(it) }
}
