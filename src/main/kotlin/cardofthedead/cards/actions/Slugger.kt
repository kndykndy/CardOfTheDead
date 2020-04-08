package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

class Slugger(gameContext: Game) : Action(gameContext, 1) {

    /**
     * Discard a zombie or take a card from another players hand.
     */
    override fun play(playedBy: Player) {
        if (playedBy.decideToDiscardZombieOrTakeCardForSlugger()) {
            // discard a zombie

            val zombiesAround = playedBy.zombiesAround
            val singleZombies = zombiesAround.getSingleZombies()
            if (singleZombies.isNotEmpty()) {
                zombiesAround.pickCard(singleZombies.random())
                    ?.let {
                        playedBy.discard(it)
                        playedBy.events.onNext(
                            MessagesFacade.Game.ActionCards.PlaySlugger(playedBy, it)
                        )
                    }
            }
        } else {
            // take a card from player

            val playerToTakeCardFrom = playedBy.choosePlayerToTakeCardFromForSlugger()
            playerToTakeCardFrom
                .hand.pickRandomCard()
                ?.let {
                    playedBy.takeToHand(it)
                    playedBy.events.onNext(
                        MessagesFacade.Game.ActionCards.PlaySlugger(
                            playedBy, null, it, playerToTakeCardFrom
                        )
                    )
                }
        }
    }
}
