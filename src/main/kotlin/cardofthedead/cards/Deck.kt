package main.kotlin.cardofthedead.cards

import kotlin.reflect.KClass

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

    fun pickCardOfClass(cKlass: KClass<out T>): T? =
        pickCard(cards.first { it::class == cKlass })

    private fun getCardsOfClass(cKlass: KClass<out T>): List<T> =
        cards.filter { it::class == cKlass }

    fun getActionCards(): List<T> = cards.filter { it::class == Action::class }

    fun getSingleZombieCards(): List<T> =
        cards.filter { it::class == Zombie::class }
            .filter { (it as Zombie).zombiesOnCard == 1 }

    fun pickRandomCard(): Card? = pickCard(cards.shuffled().first())

    // Points relates funcs

    fun getMovementPointsSum(): Int =
        cards.filterIsInstance<Action>()
            .sumBy { it.movementPoints }

    fun getZombiesCount(): Int =
        cards.filterIsInstance<Zombie>()
            .sumBy { it.zombiesOnCard }
}
