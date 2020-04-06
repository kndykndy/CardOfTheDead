package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

class Armored(gameContext: Game) : Action(gameContext, 1) {

    /**
     * Put bitten on the bottom of the deck from your hand.
     */
    override fun play(playedBy: Player) {
        var putBittenOnBottom = false

        playedBy.hand.pickCardOfClass(Bitten::class.java)
            ?.let {
                playedBy.putOnBottom(it)
                putBittenOnBottom = true
            }

        playedBy.events.onNext(
            MessagesFacade.Game.ActionCards.PlayedArmored(playedBy, putBittenOnBottom)
        )
    }
}
