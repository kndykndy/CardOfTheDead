package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.getSingleZombies
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.players.Player

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
                zombiesToDiscard = 0
            }

            if (zombiesToDiscard != 0) {
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
