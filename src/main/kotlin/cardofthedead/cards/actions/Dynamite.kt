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
    override fun play(player: Player, playDeck: Deck<Card>) {
        val zombiesAround = player.getZombieCards()
        if (zombiesAround.isNotEmpty()) {
            var zombiesToDiscard = 3

            zombiesAround.getZombieCard(`Zombies!!!`::class)?.let {
                player.discard(it)
                zombiesToDiscard -= 3
            }

            if (zombiesToDiscard >= 2) {
                zombiesAround.getZombieCard(Zombies::class)?.let {
                    player.discard(it)
                    zombiesToDiscard -= 2
                }
            }

            if (zombiesToDiscard != 0) {
                zombiesAround.getSingleZombieCards()
                    .takeLast(zombiesToDiscard)
                    .forEach { player.discard(it) }
            }

            player.chooseWorstMovementCardForDynamite()?.let { player.discard(it) }
        }
    }
}
