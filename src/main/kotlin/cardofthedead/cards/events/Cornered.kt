package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.players.Player

class Cornered : Event() {

    /**
     * Discard all your movement cards.
     */
    override fun play(playedBy: Player) {
        playedBy.escapeCards.cards.forEach { playedBy.discard(it) }
    }
}
