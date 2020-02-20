package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.players.Player

class Slugger : Action(1) {

    /**
     * Discard a zombie or take a card from another players hand.
     */
    override fun play(player: Player) {
        if (player.decideToDiscardZombieOrTakeCardForSlugger()) {
            // discard a zombie

            val zombiesAround = player.zombiesAround
            val zombieCard = zombiesAround.pickCard(
                zombiesAround.getSingleZombieCards().random()
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
