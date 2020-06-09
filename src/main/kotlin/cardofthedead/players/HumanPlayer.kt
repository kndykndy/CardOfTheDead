package cardofthedead.players

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.game.EventsFacade.Game.Input.InputProvided
import cardofthedead.game.EventsFacade.Game.Input.InputRequested
import cardofthedead.game.Game
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class HumanPlayer(
    game: Game,
    name: String,
    sex: Sex
) : Player(game, name, sex) {

    private val inputEvents: PublishSubject<InputProvided> = PublishSubject.create()

    private val selectedOptions = mutableSetOf<InputOption>()

    fun publishInputEvent(event: InputProvided) = inputEvents.onNext(event)

    override fun chooseSinglePointCardsFromCandidates(n: Int) {
        val inputOptions = candidatesToHand.cards
            .mapIndexed { index, card -> InputOption(index, card) }

        publishEvent(InputRequested(this, inputOptions))

        inputEvents
            .ofType(InputProvided::class.java)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe { msg ->
                selectedOptions.addAll(
                    msg.str!!.split(',')
                        .map { it.trim() }
                        .mapNotNull { it.toIntOrNull() }
                        .map { inputOptions.first { inputOption -> inputOption.idx == it } }
                )
            }

        var awaitPeriod = 100
        while (true) {
            Thread.sleep(250)
            awaitPeriod--
            if (selectedOptions.size == 3 || awaitPeriod <= 0) break;
        }

        selectedOptions
            .forEach { candidatesToHand.pickCard(it.card)?.let(hand::addCard) }

//            .orTimeout(5, TimeUnit.SECONDS).get()
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

class InputOption(val idx: Int, val card: Card)
