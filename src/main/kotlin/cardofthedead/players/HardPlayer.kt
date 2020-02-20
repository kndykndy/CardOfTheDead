package main.kotlin.cardofthedead.players

import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.PlayCardDecision

class HardPlayer(name: String) : Player(name) {

    override fun chooseSinglePointCards(n: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun decideToPlayCardFromHand(): PlayCardDecision {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun chooseWorstCandidateForBarricade(): Card? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun chooseWorstMovementCardForDynamite(): Card? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun decideToDrawNoCardsNextTurnForHide() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun choosePlayerToGiveZombieToForLure(): Player {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun choosePlayerToTakeCardFromForSlugger(): Player {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun choosePlayerToDiscardMovementCardsFromForTripped(): Player {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun decideHowManyMovementCardsToDiscardForTripped(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
