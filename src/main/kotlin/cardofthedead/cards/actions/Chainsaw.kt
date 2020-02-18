package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.cards.getSingleZombieCards
import main.kotlin.cardofthedead.cards.getZombieCard
import main.kotlin.cardofthedead.cards.zombies.Zombies
import main.kotlin.cardofthedead.players.Player

class Chainsaw : Action(1) {

    override fun play(player: Player, playDeck: Deck<Card>, discardDeck: Deck<Card>) {
        val zombieCards = player.getZombieCards()
        if (zombieCards.isNotEmpty()) {
            zombieCards.getZombieCard(Zombies::class)?.let {
                player.discard(it, discardDeck)
                return
            }

            zombieCards.getSingleZombieCards()
                .takeLast(2)
                .forEach { player.discard(it, discardDeck) }
        }
    }
}
