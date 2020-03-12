package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.getSingleZombies
import cardofthedead.players.Player

class Slugger : Action(1) {

    /**
     * Discard a zombie or take a card from another players hand.
     */
    override fun play(playedBy: Player) {
        if (playedBy.decideToDiscardZombieOrTakeCardForSlugger()) {
            // discard a zombie

            val zombiesAround = playedBy.zombiesAround
            val zombieCard = zombiesAround.pickCard(
                zombiesAround.getSingleZombies().random()
            )
            playedBy.discard(zombieCard!!)
        } else {
            // take a card from player

            playedBy.hand.addCard(
                playedBy.choosePlayerToTakeCardFromForSlugger().hand.pickRandomCard()!!
            )
        }
    }
}
