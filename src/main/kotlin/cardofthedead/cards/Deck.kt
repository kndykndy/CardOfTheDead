package main.kotlin.cardofthedead.cards

open class Deck {

    protected val cards: MutableList<Card> = mutableListOf()

    fun size(): Int = cards.size

    fun shuffle() = cards.shuffle()

    fun addCard(card: Card) = cards.add(card)

    fun merge(deck: Deck) {
        cards.addAll(deck.cards)
        deck.cards.clear()
    }

    fun isEmpty(): Boolean = cards.isEmpty()

    fun pickTopCard(): Card? = if (!isEmpty()) cards.removeAt(cards.size - 1) else null

    fun pickActionCards(): List<Action> = cards.filterIsInstance<Action>()

}
