package main.kotlin.cardofthedead.cards

import main.kotlin.cardofthedead.cards.actions.Armored
import main.kotlin.cardofthedead.cards.events.Cornered
import main.kotlin.cardofthedead.cards.zombies.BrideZombie
import main.kotlin.cardofthedead.cards.zombies.GrannyZombie
import main.kotlin.cardofthedead.cards.zombies.LadZombie
import main.kotlin.cardofthedead.cards.zombies.RedneckZombie
import main.kotlin.cardofthedead.cards.zombies.Zombies
import main.kotlin.cardofthedead.cards.zombies.`Zombies!!!`
import kotlin.reflect.KClass

class StandardDeck : Deck() {

    private val cardTypeToAmountInDeck = mapOf(
        BrideZombie::class to CardDescriptor(5),
        LadZombie::class to CardDescriptor(5),
        RedneckZombie::class to CardDescriptor(5),
        GrannyZombie::class to CardDescriptor(5),
        Zombies::class to CardDescriptor(
            2,
            "counts as two zombie cards. cannot be discarded with slugger"
        ),
        `Zombies!!!`::class to CardDescriptor(
            1,
            "counts as three zombie cards. cannot be discarded with slugger or chainsaw"
        ),
        Armored::class to CardDescriptor(1),
        Cornered::class to CardDescriptor(1)
    )

    private class CardDescriptor(
        val amountInDeck: Int,
        val description: String = ""
    )

    init {
        repeat(cardTypeToAmountInDeck(BrideZombie::class)) { cards.add(BrideZombie()) }
        repeat(cardTypeToAmountInDeck(LadZombie::class)) { cards.add(LadZombie()) }
        repeat(cardTypeToAmountInDeck(RedneckZombie::class)) { cards.add(RedneckZombie()) }
        repeat(cardTypeToAmountInDeck(GrannyZombie::class)) { cards.add(GrannyZombie()) }
        repeat(cardTypeToAmountInDeck(Zombies::class)) { cards.add(Zombies()) }
        repeat(cardTypeToAmountInDeck(`Zombies!!!`::class)) { cards.add(`Zombies!!!`()) }

        // todo 56 cards:
        // 23 zombies
        // 27 actions
        // 6 events
    }

    private fun cardTypeToAmountInDeck(klass: KClass<out Card>): Int =
        cardTypeToAmountInDeck.getValue(klass).amountInDeck
}
