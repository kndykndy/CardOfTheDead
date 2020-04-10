package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedArmored
import cardofthedead.game.Game
import cardofthedead.players.Player

class Armored(game: Game) : Action(game, 1) {

    /**
     * Put bitten on the bottom of the deck from your hand.
     */
    override fun play(playedBy: Player) {
        var putBittenOnBottom = false

        playedBy
            .hand
            .pickCardOfClass(Bitten::class.java)
            ?.let {
                playedBy.putOnBottom(it)
                putBittenOnBottom = true
            }

        playedBy.publishEvent(PlayedArmored(playedBy, putBittenOnBottom))
    }
}
