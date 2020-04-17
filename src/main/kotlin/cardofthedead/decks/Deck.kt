package cardofthedead.decks

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.Zombie
import cardofthedead.game.Game

open class Deck<T : Card>(
    val game: Game
) {

    // todo think over removing internal modifier -- check if it's accessed from ui
    internal val cards: MutableList<T> = mutableListOf()

    // Service funcs

    fun size(): Int = cards.size

    fun isEmpty(): Boolean = cards.isEmpty()
    fun isNotEmpty(): Boolean = cards.isNotEmpty()

    fun shuffle() = cards.shuffle()

    // Adding cards

    fun addCard(card: T) {
        if (!cards.contains(card)) cards.add(card)
    }

    fun addCardOnBottom(card: T) = cards.add(0, card)

    fun merge(deck: Deck<out T>) {
        cards.addAll(deck.cards)
        deck.cards.clear()
    }

    // Checking cards

    fun hasCard(card: T): Boolean = cards.contains(card)

    fun hasCardOfClass(cKlass: Class<out T>): Boolean =
        cards.filterIsInstance(cKlass).firstOrNull() != null

    // Picking cards

    fun pickCard(card: T): T? = if (cards.remove(card)) card else null

    fun pickTopCard(): T? = if (isNotEmpty()) pickCard(cards.last()) else null

    fun pickCardOfClass(cKlass: Class<out T>): T? =
        cards
            .filterIsInstance(cKlass)
            .firstOrNull()
            ?.let(::pickCard)

    fun pickRandomCard(): T? = if (isNotEmpty()) pickCard(cards.random()) else null

    override fun toString(): String =
        "[${cards.joinToString(",") { it::class.simpleName + it.hashCode() }}]"
}

class EmptyDeck(game: Game) : Deck<Card>(game)

enum class DeckType { STANDARD, EMPTY }

fun Deck<Card>.getActions() = this.cards.filterIsInstance<Action>()
fun Deck<Action>.getMovementPoints(): Int = this.cards.sumBy { it.movementPoints }
fun Deck<Zombie>.getZombiesAroundCount(): Int = this.cards.sumBy { it.zombiesOnCard }
fun Deck<Zombie>.getSingleZombies(): List<Zombie> = this.cards.filter { it.zombiesOnCard == 1 }
