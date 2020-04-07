package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.Zombie
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

class Lure(gameContext: Game) : Action(gameContext, 1) {

    /**
     * Give one of your zombie cards to any player.
     */
    override fun play(playedBy: Player) {
        var toPlayer: Player? = null
        var zombie: Zombie? = null

        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            val singleZombies = zombiesAround.getSingleZombies()
            if (singleZombies.isNotEmpty()) {
                zombiesAround.pickCard(singleZombies.random())
                    ?.let {
                        val playerToGiveZombieTo = playedBy.choosePlayerToGiveZombieToForLure()
                        playerToGiveZombieTo.chasedByZombie(it)

                        toPlayer = playerToGiveZombieTo
                        zombie = it
                    }
            }
        }

        playedBy.events.onNext(
            MessagesFacade.Game.ActionCards.PlayLure(
                playedBy, toPlayer, zombie
            )
        )
    }
}
