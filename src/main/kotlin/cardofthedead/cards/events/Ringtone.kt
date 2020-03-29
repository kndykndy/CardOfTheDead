package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.cards.getSingleZombies
import cardofthedead.game.Game
import cardofthedead.players.Player

class Ringtone(gameContext: Game) : Event(gameContext) {

    /**
     * Take one zombie card from every other player.
     */
    override fun play(playedBy: Player) {
        gameContext.players
            .filterNot { it == playedBy }
            .forEach { player ->
                val zombiesAround = player.zombiesAround
                val singleZombies = zombiesAround.getSingleZombies()
                if (singleZombies.isNotEmpty()) {
                    zombiesAround.pickCard(singleZombies.random())
                        ?.let(playedBy::chasedByZombie)
                }
            }
    }
}
