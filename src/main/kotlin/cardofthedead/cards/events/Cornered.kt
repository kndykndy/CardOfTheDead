package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.game.Game
import cardofthedead.players.Player

class Cornered(gameContext: Game) : Event(gameContext) {

    /**
     * Discard all your movement cards.
     */
    override fun play(playedBy: Player) {
        playedBy.discardEscapeCards()
    }
}
