package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.Game
import cardofthedead.players.Player

class Armored(gameContext: Game) : Action(gameContext, 1) {

    /**
     * Put bitten on the bottom of the deck from your hand.
     */
    override fun play(playedBy: Player) {
        gameContext
        playedBy.hand.pickCardOfClass(Bitten::class.java)
            ?.let(playedBy::putOnBottom)
    }
}
