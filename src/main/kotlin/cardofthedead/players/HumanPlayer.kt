package cardofthedead.players

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.cards.WayToPlayCard
import cardofthedead.decks.getPlayableActions
import cardofthedead.decks.getSinglePointActions
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

    private val inputOptions = mutableSetOf<InputOption>()
    private val selectedOptions = mutableSetOf<InputOption>()

    fun publishInputEvent(event: InputProvided) = inputEvents.onNext(event)

    init {
        inputEvents
            .ofType(InputProvided::class.java)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe { msg ->
                selectedOptions.addAll(
                    msg.selectedOptions
                        .map { inputOptions.first { inputOption -> inputOption.idx == it } }
                )
            }
    }

    override fun chooseSinglePointCardsFromCandidates(n: Int) {
        InputBuilder("choose single point cards to start round with")
            .withInputOptions(
                candidatesToHand
                    .getSinglePointActions()
                    .mapIndexed { index, card ->
                        ActionCardInputOption(index + 1, card)
                    }
            )
            .withMaxOptions(3)
            .buildAndRequest()

        selectedOptions
            .map { it as ActionCardInputOption }
            .forEach { candidatesToHand.pickCard(it.action)?.let(hand::addCard) }
    }

    override fun decideToPlayCardFromHand(): PlayCardDecision {
        InputBuilder("choose to play card from hand")
            .withInputOptions(
                listOf(PlayCardDecision.doNotPlay())
                    .plus(hand
                        .getPlayableActions()
                        .flatMap {
                            listOf(
                                PlayCardDecision(WayToPlayCard.PLAY_AS_ACTION, it),
                                PlayCardDecision(WayToPlayCard.PLAY_AS_MOVEMENT_POINTS, it)
                            )
                        })
                    .mapIndexed { index, decision ->
                        PlayCardDecisionInputOption(index + 1, decision)
                    }
            ).buildAndRequest()

        val decisionInputOption = selectedOptions.first() as PlayCardDecisionInputOption
        decisionInputOption.decision.card?.let { hand.pickCard(it) }
        return decisionInputOption.decision
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

    inner class InputBuilder(
        private val title: String
    ) {

        private var builderInputOptions = emptySet<InputOption>()
        private var builderMaxOptions = 1

        fun withInputOptions(inputOptions: List<InputOption>) = apply {
            builderInputOptions = inputOptions.toSet()
        }

        fun withMaxOptions(maxOptions: Int) = apply { builderMaxOptions = maxOptions }

        fun buildAndRequest() {
            inputOptions.clear()
            selectedOptions.clear()

            inputOptions.addAll(builderInputOptions)

            publishEvent(InputRequested(this@HumanPlayer, inputOptions, builderMaxOptions, title))

            while (true) {
                Thread.sleep(250)
                if (selectedOptions.isNotEmpty()) break
            }
        }
    }
}

abstract class InputOption(val idx: Int)

class ActionCardInputOption(idx: Int, val action: Action) : InputOption(idx) {

    override fun toString(): String {
        return "$idx - ${action.title}(${action.movementPoints})"
    }
}

class PlayCardDecisionInputOption(idx: Int, val decision: PlayCardDecision) : InputOption(idx) {

    override fun toString(): String {
        return "$idx - $decision"
    }
}
