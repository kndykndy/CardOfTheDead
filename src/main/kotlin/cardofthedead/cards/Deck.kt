package main.kotlin.cardofthedead.cards

open class Deck<T : Card> {

    internal val cards: MutableList<T> = mutableListOf()

    // Service funcs

    fun size(): Int = cards.size

    fun isEmpty(): Boolean = cards.isEmpty()
    fun isNotEmpty(): Boolean = cards.isNotEmpty()

    fun shuffle() = cards.shuffle()

    // Adding cards

    fun addCard(card: T) = cards.add(card)

    fun addCardOnBottom(card: T) = cards.add(0, card)

    fun merge(deck: Deck<out T>) {
        cards.addAll(deck.cards)
        deck.cards.clear()
    }

    // Picking cards

    fun pickCard(card: T): T? = if (cards.remove(card)) card else null

    fun pickTopCard(): T? = if (isNotEmpty()) pickCard(cards[cards.size - 1]) else null

    fun pickCardOfClass(cKlass: Class<out T>): T? {
        return cards.filterIsInstance(cKlass)
            .firstOrNull()
            ?.let { pickCard(it) }
    }

    fun pickRandomCard(): Card? = pickCard(cards.random())
}

fun Deck<Card>.getActions() = this.cards.filterIsInstance<Action>()

fun Deck<Action>.getMovementPoints(): Int = this.cards.sumBy { it.movementPoints }

fun Deck<Zombie>.getZombiesCount(): Int = this.cards.sumBy { it.zombiesOnCard }
fun Deck<Zombie>.getSingleZombies(): List<Zombie> = this.cards.filter { it.zombiesOnCard == 1 }
