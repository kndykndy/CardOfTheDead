package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.cards.getSingleZombieCards
import main.kotlin.cardofthedead.cards.getZombieCard
import main.kotlin.cardofthedead.cards.zombies.Zombies
import main.kotlin.cardofthedead.cards.zombies.`Zombies!!!`
import main.kotlin.cardofthedead.players.Player

class Dynamite : Action(2) {

    /**
     * Discard three zombie cards & one of your movement cards.
     */
    override fun play(player: Player, playDeck: Deck<Card>, discardDeck: Deck<Card>) {
        val zombieCards = player.getZombieCards()
        if (zombieCards.isNotEmpty()) {
            var zombiesToDiscard = 3

            zombieCards.getZombieCard(`Zombies!!!`::class)?.let {
                player.discard(it, discardDeck)
                zombiesToDiscard -= 3
            }

            if (zombiesToDiscard >= 2) {
                zombieCards.getZombieCard(Zombies::class)?.let {
                    player.discard(it, discardDeck)
                    zombiesToDiscard -= 2
                }
            }

            if (zombiesToDiscard != 0) {
                zombieCards.getSingleZombieCards()
                    .takeLast(zombiesToDiscard)
                    .forEach { player.discard(it, discardDeck) }
            }

            player.discard(player.chooseWorstMovementCardForDynamite(), discardDeck)
        }
    }
}
