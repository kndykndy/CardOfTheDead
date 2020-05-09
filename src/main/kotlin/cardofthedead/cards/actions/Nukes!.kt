package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedNukes
import cardofthedead.game.Game
import cardofthedead.players.Player

@Suppress("ClassName")
class `Nukes!`(
    game: Game
) : Action(
    game,
    "Nukes!",
    "discard all zombie cards and all cards in hand from all players (including yourself)",
    2
) {

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

        playedBy.publishEvent(PlayedNukes(playedBy))
    }
}
