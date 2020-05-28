package cardofthedead.players

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.game.EventsFacade.Game.Input.InputProvided
import cardofthedead.game.EventsFacade.Game.Input.InputRequested
import cardofthedead.game.Game
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class HumanPlayer(
    game: Game,
    name: String,
    sex: Sex
) : Player(game, name, sex) {

    private val inputEvents: PublishSubject<InputProvided> = PublishSubject.create()

    private val strs = mutableListOf<String>()

    fun publishInputEvent(event: InputProvided) = inputEvents.onNext(event)

    override fun chooseSinglePointCardsFromCandidates(n: Int) {
        publishEvent(InputRequested(this))

        inputEvents
            .ofType(InputProvided::class.java)
            .subscribe { msg ->
                strs.add(msg.str)
            }

        CompletableFuture
            .supplyAsync(this::compute)
            .orTimeout(2, TimeUnit.SECONDS).get()
    }

    fun compute() {
        if (strs.size != 3) {
            Thread.sleep(100)
        }
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
