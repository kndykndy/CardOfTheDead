package main.kotlin.cardofthedead.cards

open class Deck {

    protected val cards: MutableList<Card> = mutableListOf()

    fun size(): Int = cards.size

    fun isEmpty(): Boolean = cards.isEmpty()

    fun shuffle() = cards.shuffle()

    fun addCard(card: Card?) = card?.let { cards.add(card) }

    fun merge(deck: Deck) {
        cards.addAll(deck.cards)
        deck.cards.clear()
    }

    fun pickCard(card: Card): Card? = if (cards.remove(card)) card else null

    fun pickTopCard(): Card? = if (!isEmpty()) pickCard(cards[cards.size - 1]) else null

    fun pickActionCards(): Deck {
        val actionCardsDeck = Deck()
        cards.filterIsInstance<Action>()
            .forEach { actionCardsDeck.addCard(pickCard(it)) }
        return actionCardsDeck
    }

    fun pickRandomCards(n: Int): Deck {
        val randomCardsDeck = Deck()
        cards.shuffled()
            .take(n)
            .forEach { randomCardsDeck.addCard(pickCard(it)) }
        return randomCardsDeck
    }

}
