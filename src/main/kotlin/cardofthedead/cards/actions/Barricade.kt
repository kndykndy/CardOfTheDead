package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.players.Player

class Barricade : Action(1) {

    override fun play(
        player: Player,
        playDeck: Deck<Card>
    ) {
        player.pickCards(playDeck, 3)

        playDeck.addCardOnBottom(player.chooseWorstCandidateForBarricade())

        repeat(2) {
            player.takeTopCandidateToHand()
        }
    }
}
