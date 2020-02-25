package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.getSingleZombies
import main.kotlin.cardofthedead.cards.zombies.Zombies
import main.kotlin.cardofthedead.cards.zombies.`Zombies!!!`
import main.kotlin.cardofthedead.players.Player

class Dynamite : Action(2) {

    /**
     * Discard three zombie cards & one of your movement cards.
     */
    override fun play(player: Player) {
        val zombiesAround = player.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            var zombiesToDiscard = 3

            zombiesAround.pickCardOfClass(`Zombies!!!`::class.java)?.let {
                player.discard(it)
                zombiesToDiscard -= 3
            }

            if (zombiesToDiscard >= 2) {
                zombiesAround.pickCardOfClass(Zombies::class.java)?.let {
                    player.discard(it)
                    zombiesToDiscard -= 2
                }
            }

            if (zombiesToDiscard != 0) {
                zombiesAround.getSingleZombies()
                    .takeLast(zombiesToDiscard)
                    .forEach { player.discard(it) }
            }

            player.chooseWorstMovementCardForDynamite()?.let { player.discard(it) }
        }
    }
}
