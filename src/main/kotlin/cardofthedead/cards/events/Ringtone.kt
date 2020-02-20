package main.kotlin.cardofthedead.cards.events

import main.kotlin.cardofthedead.cards.Event
import main.kotlin.cardofthedead.players.Player

class Ringtone : Event() {

    override fun play(player: Player) {
        println("${player.name}'s skipping ${this::class.simpleName}")
    }
}
