package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.cards.actions.Slugger
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedMobs
import cardofthedead.game.Game
import cardofthedead.players.Player

class Mobs(
    game: Game
) : Event(
    game,
    "Mobs",
    "If you have a slugger at hand, ignore the event and pass it to the next player to the left. " +
            "Otherwise put all your hand on the bottom of the deck."
) {

    override fun play(playedBy: Player) {
        val playersWereMobbedMap = mutableMapOf<Player, Boolean>()

        var currentPlayer = playedBy
        do {
            val hand = currentPlayer.hand
            if (!hand.hasCardOfClass(Slugger::class.java)) {
                hand.cards
                    .toList()
                    .forEach { hand.pickCard(it)?.let(game.playDeck::addCardOnBottom) }

                playersWereMobbedMap[currentPlayer] = true

                break
            }

            playersWereMobbedMap[currentPlayer] = false
            currentPlayer = game.getNextPlayer(currentPlayer)
        } while (currentPlayer != playedBy)

        playedBy.publishEvent(PlayedMobs(playedBy, playersWereMobbedMap))
    }
}
