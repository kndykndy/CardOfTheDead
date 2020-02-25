package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.players.Player

class Mobs : Event() {

    override fun play(player: Player) {
        println("${player.name}'s skipping ${this::class.simpleName}")
    }
}
