package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedCornered
import cardofthedead.game.Game
import cardofthedead.players.Player

class Cornered(
    game: Game
) : Event(
    game,
    "Cornered",
    "Discard all your movement cards."
) {

    override fun play(playedBy: Player) {
        playedBy.discardEscapeCards()

        playedBy.publishEvent(PlayedCornered(playedBy))
    }
}
