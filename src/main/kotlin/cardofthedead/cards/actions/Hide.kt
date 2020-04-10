package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.Zombie
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

class Hide(game: Game) : Action(game, 1) {

    /**
     * Give one of your Zombie cards to the next player.
     * You may choose to draw no cards on your next turn.
     */
    override fun play(playedBy: Player) {
        var toPlayer: Player? = null
        var gaveZombie: Zombie? = null

        val zombiesAround = playedBy.zombiesAround
        if (zombiesAround.isNotEmpty()) {
            val singleZombies = zombiesAround.getSingleZombies()
            if (singleZombies.isNotEmpty()) {
                zombiesAround
                    .pickCard(singleZombies.random())
                    ?.let {
                        val nextPlayer = game.getNextPlayer(playedBy)
                        nextPlayer.chasedByZombie(it)

                        toPlayer = nextPlayer
                        gaveZombie = it
                    }
            }
        }

        val decidedToDrawNoCardsNextTurn = playedBy.decideToDrawNoCardsNextTurnForHide()

        playedBy.events.onNext(
            MessagesFacade.Game.ActionCards.PlayedHide(
                playedBy, gaveZombie, toPlayer, decidedToDrawNoCardsNextTurn
            )
        )
    }
}
