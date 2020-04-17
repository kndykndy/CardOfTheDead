package cardofthedead.players

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Barricade
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Lure
import cardofthedead.cards.actions.Pillage
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.actions.Tripped
import cardofthedead.cards.actions.`Nukes!`
import cardofthedead.cards.events.Cornered
import cardofthedead.cards.events.Fog
import cardofthedead.cards.events.Horde
import cardofthedead.cards.events.Mobs
import cardofthedead.cards.events.Ringtone
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.RedneckZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.Game

class HardPlayer(
    game: Game,
    name: String,
    sex: Sex
) : Player(game, name, sex) {

    /**
     * Worst cards rating. The bigger the better.
     */
    private val cardTypeToRating = mapOf(
        Bitten::class to 0,
        `Zombies!!!`::class to 1,
        Zombies::class to 2,
        BrideZombie::class to 3,
        GrannyZombie::class to 3,
        LadZombie::class to 3,
        RedneckZombie::class to 3,

        Cornered::class to 10, // Subject for dynamic rating evaluation
        Ringtone::class to 11,
        Mobs::class to 12, // Subject for dynamic rating evaluation
        Fog::class to 13,
        Horde::class to 14,

        // All Actions are subjects for dynamic rating evaluation
        Armored::class to 20,
        Barricade::class to 21,
        Chainsaw::class to 22,
        Hide::class to 23,
        Lure::class to 24,
        Slugger::class to 25,
        Tripped::class to 26,
        Pillage::class to 27,
        Dynamite::class to 28,
        `Nukes!`::class to 29
    )

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

    override fun chooseWorstMovementCardForDynamite(): Action? {
        TODO("not implemented")
    }

    override fun decideToDrawNoCardsNextTurnForHide(): Boolean {
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
