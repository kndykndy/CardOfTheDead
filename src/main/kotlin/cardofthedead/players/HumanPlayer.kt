package cardofthedead.players

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.game.Game

class HumanPlayer(
    game: Game,
    name: String,
    sex: Sex
) : Player(game, name, sex) {

    override fun chooseSinglePointCardsFromCandidates(n: Int) {
        TODO("Not yet implemented")
    }

    override fun decideToPlayCardFromHand(): PlayCardDecision {
        TODO("Not yet implemented")
    }

    override fun chooseWorstCandidateForBarricade(): Card? {
        TODO("Not yet implemented")
    }

    override fun chooseWorstMovementCardForDynamite(): Action? {
        TODO("Not yet implemented")
    }

    override fun decideToDrawNoCardsNextTurnForHide(): Boolean {
        TODO("Not yet implemented")
    }

    override fun choosePlayerToGiveZombieToForLure(): Player {
        TODO("Not yet implemented")
    }

    override fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean {
        TODO("Not yet implemented")
    }

    override fun choosePlayerToTakeCardFromForSlugger(): Player {
        TODO("Not yet implemented")
    }

    override fun choosePlayerToDiscardMovementCardsFromForTripped(): Player {
        TODO("Not yet implemented")
    }

    override fun decideHowManyMovementCardsToDiscardForTripped(): Int {
        TODO("Not yet implemented")
    }

}
