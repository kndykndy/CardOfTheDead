package main.kotlin.cardofthedead.cards

open class Deck {

    protected val cards: MutableList<Card> = mutableListOf()

    fun shuffle() = cards.shuffle()

    fun merge(deck: Deck) {
        cards.addAll(deck.cards)
        deck.cards.clear()
    }

    fun isEmpty(): Boolean = cards.isEmpty()
}
