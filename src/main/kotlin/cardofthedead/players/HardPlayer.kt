package cardofthedead.players

import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision

class HardPlayer(name: String) : Player(name) {

    override fun chooseSinglePointCards(n: Int) {
        TODO("not implemented")
    }

    override fun decideToPlayCardFromHand(): PlayCardDecision {
        // todo handle case when there's no sense to play Armored without Bitten at hand
        TODO("not implemented")
    }

    override fun chooseWorstCandidateForBarricade(): Card? {
        TODO("not implemented")
    }

    override fun chooseWorstMovementCardForDynamite(): Card? {
        TODO("not implemented")
    }

    override fun decideToDrawNoCardsNextTurnForHide() {
        TODO("not implemented")
    }

    override fun choosePlayerToGiveZombieToForLure(): Player {
        TODO("not implemented")
    }

    override fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean {
        TODO("not implemented")
    }

    override fun choosePlayerToTakeCardFromForSlugger(): Player {
        TODO("not implemented")
    }

    override fun choosePlayerToDiscardMovementCardsFromForTripped(): Player {
        TODO("not implemented")
    }

    override fun decideHowManyMovementCardsToDiscardForTripped(): Int {
        TODO("not implemented")
    }
}
