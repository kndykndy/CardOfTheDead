package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.players.Player

class Horde : Event() {

    /**
     * Every survivor draws two cards in their turn till the end of a round.
     */
    override fun play(playedBy: Player) {
        gameContext.cardsToPlay = 2
    }
}
