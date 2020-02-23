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

    fun pickRandomCard(): Card? = pickCard(cards.shuffled().first())

    // Getting subsets

    fun getActionCards(): List<Action> = cards.filterIsInstance(Action::class.java)

    fun getSingleZombieCards(): List<Zombie> =
        cards.filterIsInstance(Zombie::class.java)
            .filter { it.zombiesOnCard == 1 }

    // Points relates funcs

    fun getMovementPointsSum(): Int =
        cards.filterIsInstance<Action>()
            .sumBy { it.movementPoints }

    fun getZombiesCount(): Int =
        cards.filterIsInstance<Zombie>()
            .sumBy { it.zombiesOnCard }
}
