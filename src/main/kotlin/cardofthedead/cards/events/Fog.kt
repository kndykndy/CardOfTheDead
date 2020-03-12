package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.players.Player

class Fog : Event() {

    override fun play(playedBy: Player) {
        println("${playedBy.name}'s skipping ${this::class.simpleName}")
    }
}
