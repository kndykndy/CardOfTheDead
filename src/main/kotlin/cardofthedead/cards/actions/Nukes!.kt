package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

@Suppress("ClassName")
class `Nukes!`(game: Game) : Action(game, 2) {

    /**
     * Discard all zombie cards and all cards in hand from all players (including yourself).
     */
    override fun play(playedBy: Player) {
        game
            .players
            .forEach { player ->
                player.discardZombiesAround()
                player.discardHand()
            }

        playedBy.events.onNext(MessagesFacade.Game.ActionCards.PlayedNukes(playedBy))
    }
}
