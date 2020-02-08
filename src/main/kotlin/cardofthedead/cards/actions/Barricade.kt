package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.players.Player

class Barricade: Action(1) {

    override fun play(player: Player) {
        // take top three cards from deck
        // player: decide which one is worse
        // put worse under deck
        // take others to hand
    }
}
