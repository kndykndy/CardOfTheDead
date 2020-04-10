package cardofthedead.decks

import cardofthedead.cards.Card
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Barricade
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Lure
import cardofthedead.cards.actions.Pillage
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.actions.Tripped
import cardofthedead.cards.actions.`Nukes!`
import cardofthedead.cards.events.Cornered
import cardofthedead.cards.events.Fog
import cardofthedead.cards.events.Horde
import cardofthedead.cards.events.Mobs
import cardofthedead.cards.events.Ringtone
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.RedneckZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.Game
import kotlin.reflect.KClass

class StandardDeck(
    game: Game
) : Deck<Card>(game) {

    /**
     * 56 cards in total
     */
    private val cardTypeToCardDescriptor = mapOf(

        // 27 actions
        Armored::class to CardDescriptor(
            2,
            "put bitten on the bottom of the deck from your hand"
        ),
        Barricade::class to CardDescriptor(
            2,
            "draw three cards. choose one and put it on the bottom of the deck"
        ),
        Bitten::class to CardDescriptor(
            1,
            "you're not getting survival points if this card's in your hand by the end of " +
                    "the round. this card cannot be played as a movement card."
        ),
        Chainsaw::class to CardDescriptor(
            4,
            "discard two zombie cards"
        ),
        Dynamite::class to CardDescriptor(
            1,
            "discard three zombie cards & one of your movement cards"
        ),
        Hide::class to CardDescriptor(
            4,
            "give one of your Zombie cards to the next player. " +
                    "you may choose to draw no cards on your next turn."
        ),
        Lure::class to CardDescriptor(
            4,
            "give one of your zombie cards to any player"
        ),
        `Nukes!`::class to CardDescriptor(
            1,
            "discard all zombie cards and all cards in hand from all players (including yourself)"
        ),
        Pillage::class to CardDescriptor(
            1,
            "blindly draw a card from every other player"
        ),
        Slugger::class to CardDescriptor(
            4,
            "discard a zombie or take a card from another players hand"
        ),
        Tripped::class to CardDescriptor(
            3,
            "discard up to two of another player's newest movement cards"
        ),

        // 6 events
        Cornered::class to CardDescriptor(
            1,
            "discard all your movement cards"
        ),
        Fog::class to CardDescriptor(
            2,
            "all survivors take their zombies at hand. turn by turn players draw a card from " +
                    "the player to the right. and then once more. survivors show their zombies."
        ),
        Mobs::class to CardDescriptor(
            1,
            "if you have a slugger at hand, ignore the event and pass it to the next player to " +
                    "the left. otherwise put all your hand on the bottom of the deck"
        ),
        Horde::class to CardDescriptor(
            1,
            "every survivor draws two cards in their turn till the end of a round"
        ),
        Ringtone::class to CardDescriptor(
            1,
            "take one zombie card from every other player"
        ),

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
            repeat(cardTypeToAmountInDeck(cardType)) {
                cards.add(cardType.constructors.first().call(game))
            }
        }
    }

    private fun cardTypeToAmountInDeck(klass: KClass<out Card>): Int =
        cardTypeToCardDescriptor.getValue(klass).amountInDeck
}
