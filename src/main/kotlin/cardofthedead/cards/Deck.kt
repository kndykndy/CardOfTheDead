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

    fun pickCard(card: Card): Card? = if (cards.remove(card)) card else null

    fun pickTopCard(): Card? = if (isNotEmpty()) pickCard(cards[cards.size - 1]) else null

    fun pickCardOfClass(cKlass: KClass<out Card>): Card? =
        pickCard(cards.first { it::class == cKlass })

    fun getCardsOfClass(cKlass: KClass<out Card>): List<Card> =
        cards.filter { it::class == cKlass }

    fun getActionCards(): List<Card> = getCardsOfClass(Action::class)

    fun getSingleZombieCards(): List<Card> =
        cards.filter { it::class == Zombie::class }
            .filter { (it as Zombie).zombiesOnCard == 1 }

    fun pickRandomCard(): Card? = pickCard(cards.shuffled().first())

//    fun pickRandomCards(n: Int): List<Card> {
//        require(n > 0) { "N cannot be less than 0" } // todo and more than cards in deck
//
//        return cards.shuffled()
//            .take(n)
//            .map { pickCard(it) as Card }
//    }

    // Points relates funcs

    fun getMovementPointsSum(): Int =
        cards.filterIsInstance<Action>()
            .sumBy { it.movementPoints }

    fun getZombiesCount(): Int =
        cards.filterIsInstance<Zombie>()
            .sumBy { it.zombiesOnCard }

//    fun getInnerCards(): List<T> = cards.toList()


}

fun List<Zombie>.getZombieCard(zombieClass: KClass<out Zombie>): Zombie? =
    this.first { it::class == zombieClass }


