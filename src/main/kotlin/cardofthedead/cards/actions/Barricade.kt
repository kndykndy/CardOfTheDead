package main.kotlin.cardofthedead.cards.actions

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.players.Player

class Barricade : Action(1) {

    override fun play(player: Player, playDeck: Deck) {
        player.pickCards(playDeck, 3)

        val worstCandidateCard= player.chooseWorstCandidateForBarricade()

        playDeck.addCardToBottom(worstCandidateCard)

        player.takeToHand(playDeck.)

        // take top three cards from deck
        // player: decide which one is worse
        // put worse under deck
        // take others to hand
    }
}
