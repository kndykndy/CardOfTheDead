package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

@Suppress("ClassName")
class `Nukes!`(gameContext: Game) : Action(gameContext, 2) {

    /**
     * Discard all zombie cards and all cards in hand from all players (including yourself).
     */
    override fun play(playedBy: Player) {
        gameContext.players.forEach { player ->
            player.discardZombiesAround()
            player.discardHand()
        }

        playedBy.events.onNext(MessagesFacade.Game.ActionCards.PlayNukes(playedBy))
    }
}
