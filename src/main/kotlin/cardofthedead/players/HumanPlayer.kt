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
        requestInput(
            candidatesToHand
                .getSinglePointActions()
                .mapIndexed { index, action ->
                    ActionCardInputOption(index + 1, action)
                },
            "choose single point cards to start round with",
            3
        )

        selectedOptions
            .map { it as ActionCardInputOption }
            .forEach { candidatesToHand.pickCard(it.action)?.let(hand::addCard) }
    }

    override fun decideToPlayCardFromHand(): PlayCardDecision {
        requestInput(
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
                },
            "choose how to play from hand"
        )

        val decisionInputOption = selectedOptions.first() as PlayCardDecisionInputOption
        decisionInputOption.decision.card?.let { hand.pickCard(it) }
        return decisionInputOption.decision
    }

    override fun chooseWorstCandidateForBarricade(): Card? {
        requestInput(
            candidatesToHand.cards
                .mapIndexed { index, card ->
                    CardInputOption(index + 1, card)
                },
            "choose worst card"
        )

        return (selectedOptions.first() as CardInputOption).card
    }

    override fun chooseWorstMovementCardForDynamite(): Action? {
        requestInput(
            escapeCards.cards
                .mapIndexed { index, action ->
                    ActionCardInputOption(index + 1, action)
                },
            "choose worst movement card"
        )

        return (selectedOptions.first() as ActionCardInputOption).action
    }

    override fun decideToDrawNoCardsNextTurnForHide(): Boolean {
        requestInput(
            listOf(
                BooleanInputOption(1, true, "draw"),
                BooleanInputOption(2, false, "does not draw")
            ),
            "choose to draw cards next turn or not"
        )

        drawCardThisTurn = (selectedOptions.first() as BooleanInputOption).bvalue
        return drawCardThisTurn
    }

    override fun choosePlayerToGiveZombieToForLure(): Player {
        requestInput(
            game.players
                .filterNot { it == this }
                .mapIndexed { index, player ->
                    PlayerInputOption(index + 1, player)
                },
            "choose player to give zombie card to"
        )

        return (selectedOptions.first() as PlayerInputOption).player
    }

    override fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean {
        requestInput(
            listOf(
                BooleanInputOption(1, true, "discard zombie"),
                BooleanInputOption(2, false, "take card")
            ),
            "choose to discard a zombie or take a card"
        )

        return (selectedOptions.first() as BooleanInputOption).bvalue
    }

    override fun choosePlayerToTakeCardFromForSlugger(): Player {
        requestInput(
            game.players
                .filterNot { it == this }
                .mapIndexed { index, player ->
                    PlayerInputOption(index + 1, player)
                },
            "choose player to take card from"
        )

        return (selectedOptions.first() as PlayerInputOption).player
    }

    override fun choosePlayerToDiscardMovementCardsFromForTripped(): Player {
        requestInput(
            game.players
                .filterNot { it == this }
                .mapIndexed { index, player ->
                    PlayerInputOption(index + 1, player)
                },
            "choose player to discard movement cards from"
        )

        return (selectedOptions.first() as PlayerInputOption).player
    }

    override fun decideHowManyMovementCardsToDiscardForTripped(): Int {
        requestInput(
            listOf(
                BooleanInputOption(1, true, "one card"),
                BooleanInputOption(2, false, "two cards")
            ),
            "choose how many movement cards to discard"
        )

        return if ((selectedOptions.first() as BooleanInputOption).bvalue) 1 else 2
    }

    private fun requestInput(
        currentInputOptions: List<InputOption>,
        title: String,
        maxOptions: Int = 1
    ) {
        inputOptions.clear()
        selectedOptions.clear()

        inputOptions.addAll(currentInputOptions)

        publishEvent(InputRequested(this, inputOptions, maxOptions, title))

        while (true) {
            Thread.sleep(250)
            if (selectedOptions.isNotEmpty()) break
        }
    }
}

abstract class InputOption(val idx: Int)

class BooleanInputOption(
    idx: Int, val bvalue: Boolean, private val decision: String
) : InputOption(idx) {

    override fun toString() = "$idx - $decision"
}

class CardInputOption(idx: Int, val card: Card) : InputOption(idx) {

    override fun toString() =
        when (card) {
            is Action -> "$idx - ${card.title}(${card.movementPoints})"
            else -> "$idx - ${card.title}"
        }
}

class ActionCardInputOption(idx: Int, val action: Action) : InputOption(idx) {

    override fun toString() = "$idx - ${action.title}(${action.movementPoints})"
}

class PlayCardDecisionInputOption(idx: Int, val decision: PlayCardDecision) : InputOption(idx) {

    override fun toString() = "$idx - $decision"
}

class PlayerInputOption(idx: Int, val player: Player) : InputOption(idx) {

    override fun toString() = "$idx - ${player.name}"
}
