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
    private val cardTypeToCardAmountInDeck = mapOf(

        // 27 actions
        Armored::class to 2,
        Barricade::class to 2,
        Bitten::class to 1,
        Chainsaw::class to 4,
        Dynamite::class to 1,
        Hide::class to 4,
        Lure::class to 4,
        `Nukes!`::class to 1,
        Pillage::class to 1,
        Slugger::class to 4,
        Tripped::class to 3,

        // 6 events
        Cornered::class to 1,
        Fog::class to 2,
        Mobs::class to 1,
        Horde::class to 1,
        Ringtone::class to 1,

        // 23 zombies
        BrideZombie::class to 5,
        GrannyZombie::class to 5,
        LadZombie::class to 5,
        RedneckZombie::class to 5,
        Zombies::class to 2,
        `Zombies!!!`::class to 1
    )

    init {
        cardTypeToCardAmountInDeck.keys
            .forEach { cardType ->
                repeat(cardTypeToAmountInDeck(cardType)) {
                    cards.add(cardType.constructors.first().call(game))
                }
            }
    }

    private fun cardTypeToAmountInDeck(klass: KClass<out Card>): Int =
        cardTypeToCardAmountInDeck.getValue(klass)
}
