package cardofthedead.cards

open class Deck<T : Card> {

    // todo think over removing internal modifier
    internal val cards: MutableList<T> = mutableListOf()

    // Service funcs

    fun size(): Int = cards.size

    fun isEmpty(): Boolean = cards.isEmpty()
    fun isNotEmpty(): Boolean = cards.isNotEmpty()

    fun shuffle() = cards.shuffle()

    // Adding cards

    fun addCard(card: T): T {
        if (!cards.contains(card)) cards.add(card)
        return card
    }

    fun addCardOnBottom(card: T) = cards.add(0, card)

    fun merge(deck: Deck<out T>) {
        cards.addAll(deck.cards)
        deck.cards.clear()
    }

    // Checking cards

    fun hasCard(card: T): Boolean = cards.contains(card)

    fun hasCardOfClass(cKlass: Class<out T>): Boolean {
        return cards.filterIsInstance(cKlass).firstOrNull() != null
    }

    // Picking cards

    fun pickCard(card: T): T? = if (cards.remove(card)) card else null

    fun pickTopCard(): T? = if (isNotEmpty()) pickCard(cards[cards.size - 1]) else null

    fun pickCardOfClass(cKlass: Class<out T>): T? {
        return cards.filterIsInstance(cKlass)
            .firstOrNull()
            ?.let { pickCard(it) }
    }

    fun pickRandomCard(): Card? = if (isNotEmpty()) pickCard(cards.random()) else null
}

class EmptyDeck : Deck<Card>()

fun Deck<Card>.getActions() = this.cards.filterIsInstance<Action>()

fun Deck<Action>.getMovementPoints(): Int = this.cards.sumBy { it.movementPoints }

fun Deck<Zombie>.getZombiesCount(): Int = this.cards.sumBy { it.zombiesOnCard }
fun Deck<Zombie>.getSingleZombies(): List<Zombie> = this.cards.filter { it.zombiesOnCard == 1 }
