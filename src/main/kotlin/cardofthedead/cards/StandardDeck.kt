package main.kotlin.cardofthedead.cards

import main.kotlin.cardofthedead.cards.zombies.BrideZombie
import main.kotlin.cardofthedead.cards.zombies.GrannyZombie

class StandardDeck : Deck() {
    init {
        cards.add(BrideZombie())
        cards.add(GrannyZombie())

        // todo 56 cards:
        // 23 zombies
        // 27 actions
        // 6 alerts
    }
}