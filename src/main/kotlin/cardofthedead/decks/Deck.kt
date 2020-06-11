package cardofthedead.decks

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.Zombie
import cardofthedead.game.Game
import kotlin.reflect.KClass

abstract class Deck<T : Card> {

    internal val cards: MutableList<T> = mutableListOf()

    fun build(game: Game): Deck<T> {
        val cardTypeToCardAmountInDeck: Map<KClass<out T>, Int> = getCardTypeToCardAmountInDeck()

        cardTypeToCardAmountInDeck.keys
            .forEach { cardType ->
                repeat(cardTypeToCardAmountInDeck.getValue(cardType)) {
                    cards.add(cardType.constructors.first().call(game))
                }
            }

        return this
    }

    abstract fun getCardTypeToCardAmountInDeck(): Map<KClass<out T>, Int>

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
    fun pickCard(idx: Int): T? =
        if (isNotEmpty() && idx >= 0 && idx < cards.size) cards.removeAt(idx) else null

    fun pickTopCard(): T? = if (isNotEmpty()) pickCard(cards.last()) else null

    fun pickCardOfClass(cKlass: Class<out T>): T? =
        cards.filterIsInstance(cKlass)
            .firstOrNull()
            ?.let(::pickCard)

    override fun toString(): String =
        "[${cards.joinToString(",") { it.title + it.hashCode() }}]"
}

class EmptyDeck<T : Card> : Deck<T>() {

    override fun getCardTypeToCardAmountInDeck(): Map<KClass<out T>, Int> = emptyMap()
}

fun Deck<Card>.getActions() = this.cards.filterIsInstance<Action>()
fun Deck<Card>.getSinglePointActions() = this.getActions().filter { it.movementPoints == 1 }
fun Deck<Action>.getMovementPoints(): Int = this.cards.sumBy { it.movementPoints }
fun Deck<Zombie>.getZombiesAroundCount(): Int = this.cards.sumBy { it.zombiesOnCard }
fun Deck<Zombie>.getSingleZombies(): List<Zombie> = this.cards.filter { it.zombiesOnCard == 1 }
