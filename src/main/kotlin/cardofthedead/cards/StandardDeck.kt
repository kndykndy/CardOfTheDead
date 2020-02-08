package main.kotlin.cardofthedead.cards

import main.kotlin.cardofthedead.cards.actions.Armored
import main.kotlin.cardofthedead.cards.actions.Barricade
import main.kotlin.cardofthedead.cards.actions.Bitten
import main.kotlin.cardofthedead.cards.actions.Chainsaw
import main.kotlin.cardofthedead.cards.actions.Dynamite
import main.kotlin.cardofthedead.cards.actions.Hide
import main.kotlin.cardofthedead.cards.actions.Lure
import main.kotlin.cardofthedead.cards.actions.Pillage
import main.kotlin.cardofthedead.cards.actions.Slugger
import main.kotlin.cardofthedead.cards.actions.Tripped
import main.kotlin.cardofthedead.cards.actions.`Nukes!`
import main.kotlin.cardofthedead.cards.events.Cornered
import main.kotlin.cardofthedead.cards.events.Fog
import main.kotlin.cardofthedead.cards.events.Gopota
import main.kotlin.cardofthedead.cards.events.Horde
import main.kotlin.cardofthedead.cards.events.Ringtone
import main.kotlin.cardofthedead.cards.zombies.BrideZombie
import main.kotlin.cardofthedead.cards.zombies.GrannyZombie
import main.kotlin.cardofthedead.cards.zombies.LadZombie
import main.kotlin.cardofthedead.cards.zombies.RedneckZombie
import main.kotlin.cardofthedead.cards.zombies.Zombies
import main.kotlin.cardofthedead.cards.zombies.`Zombies!!!`
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class StandardDeck : Deck() {

    /**
     * 56 cards in total
     */
    private val cardTypeToCardDescriptor = mapOf(
        // 27 actions
        Armored::class to CardDescriptor(2, "put bitten on the bottom of the deck from your hand"),
        Barricade::class to CardDescriptor(
            2,
            "draw three cards. choose one an put it on the bottom of the deck"
        ),
        Bitten::class to CardDescriptor(1),
        Chainsaw::class to CardDescriptor(4, "discard two zombie cards"),
        Dynamite::class to CardDescriptor(
            1,
            "discard three zombie cards & one of your movement cards"
        ),
        Hide::class to CardDescriptor(
            4,
            "give one of your zombie cards to the next player. you may choose to draw no cards on your next turn."
        ),
        Lure::class to CardDescriptor(4, "give one of your zombie cards to any player"),
        `Nukes!`::class to CardDescriptor(
            1,
            "discard all zombie cards and all cards in hand from all players"
        ),
        Pillage::class to CardDescriptor(1),
        Slugger::class to CardDescriptor(
            4,
            "discard a zombie or take a card from another players hand"
        ),
        Tripped::class to CardDescriptor(
            3,
            "discard up to two of another player's newest movement cards"
        ),

        // 6 events
        Cornered::class to CardDescriptor(1, "discard all your movement cards"),
        Fog::class to CardDescriptor(2),
        Gopota::class to CardDescriptor(1),
        Horde::class to CardDescriptor(1),
        Ringtone::class to CardDescriptor(1, "take one zombie card from every other player"),

        // 23 zombies
        BrideZombie::class to CardDescriptor(5),
        GrannyZombie::class to CardDescriptor(5),
        LadZombie::class to CardDescriptor(5),
        RedneckZombie::class to CardDescriptor(5),
        Zombies::class to CardDescriptor(
            2,
            "counts as two zombie cards. cannot be discarded with slugger"
        ),
        `Zombies!!!`::class to CardDescriptor(
            1,
            "counts as three zombie cards. cannot be discarded with slugger or chainsaw"
        )
    )

    private class CardDescriptor(
        val amountInDeck: Int,
        val description: String = ""
    )

    init {
        cardTypeToCardDescriptor.keys.forEach { cardType ->
            repeat(cardTypeToAmountInDeck(cardType)) { cards.add(cardType.createInstance()) }
        }

//        repeat(cardTypeToAmountInDeck(BrideZombie::class)) { cards.add(BrideZombie()) }
//        repeat(cardTypeToAmountInDeck(LadZombie::class)) { cards.add(LadZombie()) }
//        repeat(cardTypeToAmountInDeck(RedneckZombie::class)) { cards.add(RedneckZombie()) }
//        repeat(cardTypeToAmountInDeck(GrannyZombie::class)) { cards.add(GrannyZombie()) }
//        repeat(cardTypeToAmountInDeck(Zombies::class)) { cards.add(Zombies()) }
//        repeat(cardTypeToAmountInDeck(`Zombies!!!`::class)) { cards.add(`Zombies!!!`()) }
    }

    private fun cardTypeToAmountInDeck(klass: KClass<out Card>): Int =
        cardTypeToCardDescriptor.getValue(klass).amountInDeck
}
