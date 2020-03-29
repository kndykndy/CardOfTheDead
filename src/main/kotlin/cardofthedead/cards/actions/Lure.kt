package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.getSingleZombies
import cardofthedead.game.Game
import cardofthedead.players.Player

class Lure(gameContext: Game) : Action(gameContext, 1) {

    /**
     * Give one of your zombie cards to any player.
     */
    override fun play(playedBy: Player) {
        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            val singleZombies = zombiesAround.getSingleZombies()
            if (singleZombies.isNotEmpty()) {
                zombiesAround.pickCard(singleZombies.random())
                    ?.let(playedBy.choosePlayerToGiveZombieToForLure()::chasedByZombie)
            }
        }
    }
}
