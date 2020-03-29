package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.getSingleZombies
import cardofthedead.game.Game
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
                    ?.let(playedBy::discard)
            }
        } else {
            // take a card from player

            playedBy
                .choosePlayerToTakeCardFromForSlugger()
                .hand.pickRandomCard()
                ?.let(playedBy::takeToHand)
        }
    }
}
