package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedRingtone
import cardofthedead.game.Game
import cardofthedead.players.Player

class Ringtone(game: Game) : Event(game) {

    /**
     * Take one zombie card from every other player.
     */
    override fun play(playedBy: Player) {
        val initialZombiesAroundCount = playedBy.getZombiesAroundCount()

        game.players
            .filterNot { it == playedBy }
            .forEach { player ->
                val zombiesAround = player.zombiesAround
                val singleZombies = zombiesAround.getSingleZombies()
                if (singleZombies.isNotEmpty()) {
                    zombiesAround.pickCard(singleZombies.random())
                        ?.let(playedBy::chasedByZombie)
                }
            }

        playedBy.publishEvent(
            PlayedRingtone(
                playedBy,
                initialZombiesAroundCount,
                playedBy.getZombiesAroundCount()
            )
        )
    }
}
