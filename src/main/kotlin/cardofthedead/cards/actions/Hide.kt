package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.Zombie
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

class Hide(gameContext: Game) : Action(gameContext, 1) {

    /**
     * Give one of your Zombie cards to the next player.
     * You may choose to draw no cards on your next turn.
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
                        val nextPlayer = gameContext.getNextPlayer(playedBy)
                        nextPlayer.chasedByZombie(it)

                        toPlayer = nextPlayer
                        zombie = it
                    }
            }
        }

        val decideToDrawNoCardsNextTurn = playedBy.decideToDrawNoCardsNextTurnForHide()

        playedBy.events.onNext(
            MessagesFacade.Game.ActionCards.PlayHide(
                playedBy, toPlayer, zombie, decideToDrawNoCardsNextTurn
            )
        )
    }
}
