package main.kotlin.cardofthedead.players

class EasyPlayer(name: String) : Player(name, Level.EASY) {

    /**
     * Picks random N cards from the candidates deck.
     */
    override fun chooseSinglePointCards(n: Int) {
        val actionCardsDeck = candidatesToHand.pickActionCards()
        if (actionCardsDeck.size() > n) {
            hand.merge(actionCardsDeck.pickRandomCards(n))
            candidatesToHand.merge(actionCardsDeck)
        } else {
            hand.merge(actionCardsDeck)
        }
    }

}