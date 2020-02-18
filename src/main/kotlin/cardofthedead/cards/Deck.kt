package main.kotlin.cardofthedead.cards

import kotlin.reflect.KClass

open class Deck<T : Card> {

    protected val cards: MutableList<T> = mutableListOf()

    fun size(): Int = cards.size

    fun isEmpty(): Boolean = cards.isEmpty()

    fun shuffle() = cards.shuffle()

    fun addCard(card: T?) = card?.let { cards.add(card) }

    fun addCardOnBottom(card: T?) = card?.let { cards.add(0, card) }

    fun merge(deck: Deck<out T>) {
        cards.addAll(deck.cards)
        deck.cards.clear()
    }

    fun pickTopCard(): Card? = if (!isEmpty()) pickCard(cards[cards.size - 1]) else null

    // todo think over making this function impossible without "to deck" -- where to pick to
    fun pickCard(card: Card): Card? = if (cards.remove(card)) card else null

    fun pickActionCards(): Deck<Action> {
        val actionCardsDeck = Deck<Action>()
        cards.filterIsInstance<Action>()
            .forEach { actionCardsDeck.addCard(pickCard(it) as Action?) }
        return actionCardsDeck
    }

    fun pickRandomCard(): Card = cards.shuffled().first()

    fun pickRandomCards(n: Int): Deck<Card> {
        require(n > 0) { "N cannot be less than 0" } // todo and more than cards in deck

        val randomCardsDeck = Deck<Card>()
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

    fun getInnerCards(): List<T> = cards.toList()

    fun getCardOfClass(card: KClass<out Card>): Card? = cards.first { it::class == card }

    fun getCardsOfClass(card: KClass<out Card>): List<Card> = cards.filter { it::class == card }
}

fun List<Zombie>.getZombieCard(zombieClass: KClass<out Zombie>): Zombie? =
    this.first { it::class == zombieClass }

fun List<Zombie>.getSingleZombieCards(): List<Zombie> =
    this.filter { it::class == Zombie::class }
        .filter { it.zombiesOnCard == 1 }
