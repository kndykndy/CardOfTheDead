package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.getSingleZombies
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.Game
import cardofthedead.players.Player

class Dynamite(gameContext: Game) : Action(gameContext, 2) {

    /**
     * Discard three zombie cards & one of your movement cards.
     */
    override fun play(playedBy: Player) {
        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            var zombiesToDiscard = 3

            zombiesAround.pickCardOfClass(`Zombies!!!`::class.java)?.let {
                playedBy.discard(it)
                zombiesToDiscard = 0
            }

            if (zombiesToDiscard != 0) {
                zombiesAround.pickCardOfClass(Zombies::class.java)?.let {
                    playedBy.discard(it)
                    zombiesToDiscard -= 2
                }
            }

            if (zombiesToDiscard != 0) {
                zombiesAround.getSingleZombies()
                    .takeLast(zombiesToDiscard)
                    .forEach { zombiesAround.pickCard(it)?.let(playedBy::discard) }
            }
        }

        playedBy.chooseWorstMovementCardForDynamite()
            ?.let(playedBy::discard)
    }
}
