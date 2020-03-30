package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.decks.getSingleZombies
import cardofthedead.cards.zombies.Zombies
import cardofthedead.game.Game
import cardofthedead.players.Player

class Chainsaw(gameContext: Game) : Action(gameContext, 1) {

    /**
     * Discard two zombie cards.
     */
    override fun play(playedBy: Player) {
        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            zombiesAround.pickCardOfClass(Zombies::class.java)
                ?.let(playedBy::discard)
                ?: zombiesAround.getSingleZombies()
                    .takeLast(2)
                    .forEach { zombiesAround.pickCard(it)?.let(playedBy::discard) }
        }
    }
}
