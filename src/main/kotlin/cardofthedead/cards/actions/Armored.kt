package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.players.Player

class Armored : Action(1) {

    override fun play(player: Player) {
        player.hand.pickCardOfClass(Bitten::class)?.let {
            player.putOnBottom(it)
        }
    }
}
