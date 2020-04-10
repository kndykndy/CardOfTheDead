package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.game.Game
import cardofthedead.players.Player

class Horde(game: Game) : Event(game) {

    /**
     * Every survivor draws two cards in their turn till the end of a round.
     */
    override fun play(playedBy: Player) {
        game.cardsToPlay = 2
    }
}
