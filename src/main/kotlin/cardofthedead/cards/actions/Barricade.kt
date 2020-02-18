package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.players.Player

class Barricade : Action(1) {

    override fun play(player: Player) {
        player.pickCards(3)

        gameContext.playDeck.addCardOnBottom(player.chooseWorstCandidateForBarricade())

        repeat(2) {
            player.takeTopCandidateToHand()
        }
    }
}
