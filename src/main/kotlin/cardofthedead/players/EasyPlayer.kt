package main.kotlin.cardofthedead.players

class EasyPlayer(name: String) : Player(name, Level.EASY) {

    /**
     * Picks random N cards from the rest of the hand.
     */
    override fun chooseSinglePointCards(n: Int) {
        val actionCardsDeck = candidatesToHand.pickActionCards()
        if (actionCardsDeck.size() > n) {
            val randomCards = actionCardsDeck.pickRandomCards(n)
            hand.merge(randomCards)
            candidatesToHand.merge(actionCardsDeck)
        } else {
            hand.merge(actionCardsDeck)
        }
    }

}