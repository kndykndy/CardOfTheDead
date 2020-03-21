package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.cards.getSingleZombies
import cardofthedead.players.Player

class Ringtone : Event() {

    /**
     * Take one zombie card from every other player.
     */
    override fun play(playedBy: Player) {
        gameContext.players.forEach { player ->
            if (player != playedBy) {
                val zombiesAround = player.zombiesAround
                zombiesAround.getSingleZombies()
                    .random() // todo will fail if empty
                    .also { zombiesAround.pickCard(it)?.let(playedBy::chasedByZombie) }
            }
        }
    }
}
