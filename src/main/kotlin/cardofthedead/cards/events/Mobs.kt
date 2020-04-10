package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.cards.actions.Slugger
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

class Mobs(game: Game) : Event(game) {

    /**
     * If you have a slugger at hand, ignore the event and pass it to the next player to the left.
     * Otherwise put all your hand on the bottom of the deck.
     */
    override fun play(playedBy: Player) {
        val playersWereMobbedMap: MutableMap<Player, Boolean> = mutableMapOf()

        var currentPlayer = playedBy
        do {
            val hand = currentPlayer.hand
            if (!hand.hasCardOfClass(Slugger::class.java)) {
                val cards = hand.cards.toList()
                cards.forEach {
                    hand.pickCard(it)?.let(game.playDeck::addCardOnBottom)
                }

                playersWereMobbedMap[currentPlayer] = true

                return
            }

            playersWereMobbedMap[currentPlayer] = false
            currentPlayer = game.getNextPlayer(currentPlayer)
        } while (currentPlayer != playedBy)

        playedBy.publishEvent(
            MessagesFacade.Game.EventCards.PlayedMobs(
                playedBy,
                playersWereMobbedMap
            )
        )
    }
}
