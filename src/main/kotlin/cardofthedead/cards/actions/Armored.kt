package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.players.Player

class Armored : Action(1) {

    /**
     * Put bitten on the bottom of the deck from your hand.
     */
    override fun play(playedBy: Player) {
        playedBy.hand.pickCardOfClass(Bitten::class.java)
            ?.let(playedBy::putOnBottom)
    }
}
