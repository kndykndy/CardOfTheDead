package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.Zombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedChainsaw
import cardofthedead.game.Game
import cardofthedead.players.Player

class Chainsaw(game: Game) : Action(game, 1) {

    /**
     * Discard two zombie cards.
     */
    override fun play(playedBy: Player) {
        val discardedZombies = mutableListOf<Zombie>()

        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            zombiesAround
                .pickCardOfClass(Zombies::class.java)
                ?.let {
                    playedBy.discard(it)
                    discardedZombies.add(it)
                }
                ?: zombiesAround
                    .getSingleZombies()
                    .takeLast(2)
                    .forEach {
                        zombiesAround.pickCard(it)?.let(playedBy::discard)
                        discardedZombies.add(it)
                    }
        }

        playedBy.publishEvent(PlayedChainsaw(playedBy, discardedZombies))
    }
}
