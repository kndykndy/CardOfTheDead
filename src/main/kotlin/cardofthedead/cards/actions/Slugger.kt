package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.getSingleZombies
import cardofthedead.players.Player

class Slugger : Action(1) {

    /**
     * Discard a zombie or take a card from another players hand.
     */
    override fun play(player: Player) {
        if (player.decideToDiscardZombieOrTakeCardForSlugger()) {
            // discard a zombie

            val zombiesAround = player.zombiesAround
            val zombieCard = zombiesAround.pickCard(
                zombiesAround.getSingleZombies().random()
            )
            player.discard(zombieCard!!)
        } else {
            // take a card from player

            player.hand.addCard(
                player.choosePlayerToTakeCardFromForSlugger().hand.pickRandomCard()!!
            )
        }
    }
}
