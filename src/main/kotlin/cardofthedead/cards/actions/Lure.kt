package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.Zombie
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedLure
import cardofthedead.game.Game
import cardofthedead.players.Player

class Lure(game: Game) : Action(game, 1) {

    /**
     * Give one of your zombie cards to any player.
     */
    override fun play(playedBy: Player) {
        var gaveZombie: Zombie? = null
        var toPlayer: Player? = null

        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            val singleZombies = zombiesAround.getSingleZombies()
            if (singleZombies.isNotEmpty()) {
                zombiesAround
                    .pickCard(singleZombies.random())
                    ?.let {
                        val playerToGiveZombieTo = playedBy.choosePlayerToGiveZombieToForLure()
                        playerToGiveZombieTo.chasedByZombie(it)

                        gaveZombie = it
                        toPlayer = playerToGiveZombieTo
                    }
            }
        }

        playedBy.publishEvent(PlayedLure(playedBy, gaveZombie, toPlayer))
    }
}
