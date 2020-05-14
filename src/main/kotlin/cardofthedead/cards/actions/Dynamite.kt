package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.Zombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedDynamite
import cardofthedead.game.Game
import cardofthedead.players.Player

class Dynamite(
    game: Game
) : Action(
    game,
    "Dynamite",
    "Discard three zombie cards & one of your movement cards.",
    2
) {

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
                playedBy.escapeCards.pickCard(it)?.let(playedBy::discard)
                discardedMovementCard = it
            }

        playedBy.publishEvent(PlayedDynamite(playedBy, discardedZombies, discardedMovementCard))
    }
}
