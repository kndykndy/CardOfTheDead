package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.Zombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

class Dynamite(game: Game) : Action(game, 2) {

    /**
     * Discard three zombie cards & one of your movement cards.
     */
    override fun play(playedBy: Player) {
        val discardedZombies = mutableListOf<Zombie>()

        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            var zombiesToDiscard = 3

            zombiesAround
                .pickCardOfClass(`Zombies!!!`::class.java)
                ?.let {
                    playedBy.discard(it)
                    zombiesToDiscard = 0
                    discardedZombies.add(it)
                }

            if (zombiesToDiscard != 0) {
                zombiesAround
                    .pickCardOfClass(Zombies::class.java)
                    ?.let {
                        playedBy.discard(it)
                        zombiesToDiscard -= 2
                        discardedZombies.add(it)
                    }
            }

            if (zombiesToDiscard != 0) {
                zombiesAround
                    .getSingleZombies()
                    .takeLast(zombiesToDiscard)
                    .forEach {
                        zombiesAround.pickCard(it)?.let(playedBy::discard)
                        discardedZombies.add(it)
                    }
            }
        }

        var discardedMovementCard: Action? = null

        playedBy
            .chooseWorstMovementCardForDynamite()
            ?.let {
                playedBy.discard(it)
                discardedMovementCard = it
            }

        playedBy.publishEvent(
            MessagesFacade.Game.ActionCards.PlayedDynamite(
                playedBy, discardedZombies, discardedMovementCard
            )
        )
    }
}
