package main.kotlin.cardofthedead.cards

import main.kotlin.cardofthedead.cards.zombies.BrideZombie
import main.kotlin.cardofthedead.cards.zombies.GrannyZombie
import main.kotlin.cardofthedead.cards.zombies.LadZombie
import main.kotlin.cardofthedead.cards.zombies.Zombies
import main.kotlin.cardofthedead.cards.zombies.`Zombies!!!`

class StandardDeck : Deck() {

    init {
        repeat(BrideZombie.CARDS_IN_FULL_DECK) { cards.add(BrideZombie()) }
        repeat(LadZombie.CARDS_IN_FULL_DECK) { cards.add(LadZombie()) }
        repeat(GrannyZombie.CARDS_IN_FULL_DECK) { cards.add(GrannyZombie()) }
        repeat(Zombies.CARDS_IN_FULL_DECK) { cards.add(Zombies()) }
        repeat(`Zombies!!!`.CARDS_IN_FULL_DECK) { cards.add(`Zombies!!!`()) }

        // todo 56 cards:
        // 23 zombies
        // 27 actions
        // 6 events
    }
}
