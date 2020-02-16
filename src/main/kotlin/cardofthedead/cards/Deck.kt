package main.kotlin.cardofthedead.cards

import kotlin.reflect.KClass

open class Deck {

    protected val cards: MutableList<Card> = mutableListOf()

    fun size(): Int = cards.size

    fun isEmpty(): Boolean = cards.isEmpty()

    fun shuffle() = cards.shuffle()

    fun addCard(card: Card?) = card?.let { cards.add(card) }

    fun addCardOnBottom(card: Card?) = card?.let { cards.add(0, card) }

    fun merge(deck: Deck) {
        cards.addAll(deck.cards)
        deck.cards.clear()
    }

    fun pickTopCard(): Card? = if (!isEmpty()) pickCard(cards[cards.size - 1]) else null

    private fun pickCard(card: Card): Card? = if (cards.remove(card)) card else null

    fun pickActionCards(): Deck {
        val actionCardsDeck = Deck()
        cards.filterIsInstance<Action>()
            .forEach { actionCardsDeck.addCard(pickCard(it)) }
        return actionCardsDeck
    }

    fun pickRandomCard(): Card = cards.shuffled().first()

    fun pickRandomCards(n: Int): Deck {
        require(n > 0) { "N cannot be less than 0" } // todo and more than cards in deck

        val randomCardsDeck = Deck()
        cards.shuffled()
            .take(n)
            .forEach { randomCardsDeck.addCard(pickCard(it)) }
        return randomCardsDeck
    }

    fun getMovementPointsSum(): Int =
        cards.filterIsInstance<Action>()
            .sumBy { it.movementPoints }

    fun getZombiesCount(): Int =
        cards.filterIsInstance<Zombie>()
            .sumBy { it.zombiesOnCard }

    fun getInnerCards(): List<Card> = cards.toList()

    fun getCardOfClass(card: KClass<out Card>): Card? = cards.first { it::class == card }

    fun getCardsOfClass(card: KClass<out Card>): List<Card> = cards.filter { it::class == card }
}
