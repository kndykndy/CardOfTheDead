package cardofthedead.game

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.Event
import cardofthedead.cards.WayToPlayCard
import cardofthedead.cards.Zombie
import cardofthedead.decks.Deck
import cardofthedead.decks.DeckType
import cardofthedead.decks.EmptyDeck
import cardofthedead.decks.StandardDeck
import cardofthedead.players.EasyPlayer
import cardofthedead.players.HardPlayer
import cardofthedead.players.Level
import cardofthedead.players.Player
import cardofthedead.players.PlayerDescriptor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.random.Random

class Game private constructor(builder: Builder) {

    var eventsQueue: Observable<MessagesFacade.Message>
    private val gameEvents: PublishSubject<MessagesFacade.Message> = PublishSubject.create()

    internal val players: MutableList<Player> = mutableListOf()

    private val deadPlayers: MutableList<Player> = mutableListOf()
    private val winners: MutableList<List<Player>> = mutableListOf()

    internal var initialPlayersCount: Int = 0

    internal val playDeck: Deck<Card> = EmptyDeck(this)
    internal val discardDeck: Deck<Card> = EmptyDeck(this)

    /**
     * Because of Horde cards per round taken by each turn will be two.
     */
    internal var cardsToPlay: Int = 1

    val playersToZombiesToBeSurrounded = mapOf(2 to 5, 3 to 4, 4 to 4, 5 to 3)
    private val playersToZombiesToBeEaten = mapOf(2 to 7, 3 to 6, 4 to 6, 5 to 5)
    private val playersToMovementPointsToEscape = mapOf(2 to 7, 3 to 6, 4 to 6, 5 to 5)

    init {
        builder.playerDescriptors
            .map { player ->
                when (player.level) {
                    Level.HARD -> HardPlayer(this, player.name, player.sex)
                    else -> EasyPlayer(this, player.name, player.sex)
                }
            }
            .forEach { players.add(it) }
        initialPlayersCount = players.size

        if (DeckType.STANDARD == builder.deckType) {
            playDeck.merge(StandardDeck(this))
        }

        eventsQueue = Observable.merge(players.map { it.events }.plus(gameEvents))

        winners.add(listOf(lastPlayerWentToShoppingMall()))
    }

    fun play() {
        gameEvents.onNext(
            MessagesFacade.Game.General.NewGame(
                playDeck.size(),
                players.size,
                players,
                winners.first().first()
            )
        )

        repeat(3) { i ->
            gameEvents.onNext(MessagesFacade.Game.Pending.NewRound(i + 1))
            playRound()
        }

        announceGameWinners()
    }

    private fun playRound() {
        prepareForRound()

        var currentPlayer = winners.last().random()
        gameEvents.onNext(MessagesFacade.Game.Pending.FirstPlayer(currentPlayer))

        while (isRoundRunning()) {
            playTurn(currentPlayer)

            currentPlayer = getNextPlayer(currentPlayer)
            gameEvents.onNext(MessagesFacade.Game.Pending.NextPlayer(currentPlayer))
        }

        announceRoundWinners()
    }

    private fun announceRoundWinners() {
        val winnersList = when {
            isRoundOverBecauseOneAlivePlayerLeft() -> {
                val winner = players.first().also {
                    it.addSurvivalPoints(5)
                }

                gameEvents.onNext(MessagesFacade.Game.Pending.RoundWinnerOneAlive(winner))

                listOf(winner)
            }
            isRoundOverBecauseEscapedPlayer() -> {
                val winner = players
                    .map { it.addSurvivalPoints(it.getMovementPoints()); it }
                    .first {
                        it.getMovementPoints() >=
                                playersToMovementPointsToEscape.getValue(initialPlayersCount)
                    }

                gameEvents.onNext(MessagesFacade.Game.Pending.RoundWinnerEscaped(winner))

                listOf(winner)
            }
            isRoundOverBecauseOfEmptyDeck() -> {
                val maxMovementPoints = players.map { it.getMovementPoints() }.max()

                val winners = players
                    .map { it.addSurvivalPoints(it.getMovementPoints()); it }
                    .filter { it.getMovementPoints() == maxMovementPoints }

                gameEvents.onNext(MessagesFacade.Game.Pending.RoundWinnersDeckOver(winners))

                winners
            }
            else -> {
                throw IllegalStateException()
            }
        }

        winners.add(winnersList)
    }

    private fun announceGameWinners() {
        players.addAll(deadPlayers)

        val maxScore = players.map { it.getSurvivalPoints() }.max()
        val winners = players.groupBy { it.getSurvivalPoints() }[maxScore]!!

        gameEvents.onNext(MessagesFacade.Game.General.WinnersAnnouncement(players, winners))
    }

    private fun playTurn(currentPlayer: Player) {
        repeat(cardsToPlay) {
            when (val drawnCard = currentPlayer.drawTopCard()) {
                is Action -> {
                    gameEvents.onNext(MessagesFacade.Game.Pending.DrawAction(drawnCard))

                    currentPlayer.takeToHand(drawnCard)
                }
                is Zombie -> {
                    gameEvents.onNext(MessagesFacade.Game.Pending.DrawZombie(drawnCard))

                    currentPlayer.chasedByZombie(drawnCard)
                    if (currentPlayer.getZombiesAroundCount() >=
                        playersToZombiesToBeEaten.getValue(initialPlayersCount)
                    ) {
                        currentPlayer.die()
                        removePlayer(currentPlayer)

                        return
                    }
                }
                is Event -> {
                    gameEvents.onNext(MessagesFacade.Game.Pending.DrawEvent(drawnCard))

                    currentPlayer.play(drawnCard)
                    currentPlayer.discard(drawnCard)
                }
                null -> {
                    return
                }
            }
        }

        val decisionToPlayCardFromHand = currentPlayer.decideToPlayCardFromHand()
        if (decisionToPlayCardFromHand.isGonnaPlay()) {
            val actionCardFromHand: Card = decisionToPlayCardFromHand.card ?: return

            if (decisionToPlayCardFromHand.wayToPlayCard == WayToPlayCard.PLAY_AS_ACTION) {
                println("${currentPlayer.name} decided to play ${actionCardFromHand::class.simpleName}.")

                currentPlayer.play(actionCardFromHand)
                currentPlayer.discard(actionCardFromHand)
            } else { // as movement points
                println("${currentPlayer.name} decided to play ${actionCardFromHand::class.simpleName} as movement points.")

                currentPlayer.addMovementPoints(actionCardFromHand as Action)
                if (currentPlayer.getMovementPoints() >=
                    playersToMovementPointsToEscape.getValue(initialPlayersCount)
                ) {
                    return
                }
            }
        } else {
            println("${currentPlayer.name} decided not to play card from hand.")
        }

        println("DEBUG: $currentPlayer")
    }

    private fun prepareForRound() {
        cardsToPlay = 1

        players.addAll(deadPlayers)
        deadPlayers.clear()

        players.forEach { it.discardAllCards() }
        playDeck.merge(discardDeck)

        playDeck.shuffle() // before initial dealing

        // initial dealing
        players.forEach { player ->
            player.pickCandidateCards(10)
            player.chooseSinglePointCards(3)
        }
        players.forEach { it.discardCandidatesCards() }
        playDeck.merge(discardDeck)

        playDeck.shuffle()
    }

    internal fun getPrevPlayer(beforePlayer: Player): Player {
        if (players.isEmpty() || (players.size == 1 && players.first() == beforePlayer))
            throw IllegalStateException()

        var result = players.last()

        val iterator = players.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next == beforePlayer) {
                return result
            } else {
                result = next
            }
        }

        return players.last()
    }

    internal fun getNextPlayer(afterPlayer: Player): Player {
        if (players.isEmpty() || (players.size == 1 && players.first() == afterPlayer))
            throw IllegalStateException()

        val iterator = players.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() == afterPlayer) {
                if (iterator.hasNext()) {
                    return iterator.next()
                }
            }
        }

        return players.first()
    }

    internal fun getRandomPlayer(exceptForPlayer: Player): Player {
        if (players.isEmpty() || (players.size == 1 && players.first() == exceptForPlayer))
            throw IllegalStateException()

        while (true) {
            val randomPlayer = players.random()
            if (randomPlayer != exceptForPlayer) {
                return randomPlayer
            }
        }
    }

    private fun lastPlayerWentToShoppingMall(): Player =
        players.elementAt(Random.nextInt(players.size))

    private fun removePlayer(player: Player) {
        players.remove(player)
        deadPlayers.add(player)
    }

    private fun isRoundRunning(): Boolean =
        !isRoundOverBecauseOneAlivePlayerLeft() &&
                !isRoundOverBecauseEscapedPlayer() &&
                !isRoundOverBecauseOfEmptyDeck()

    private fun isRoundOverBecauseOneAlivePlayerLeft(): Boolean = players.size == 1

    private fun isRoundOverBecauseEscapedPlayer(): Boolean =
        players.firstOrNull { player ->
            player.getMovementPoints() >=
                    playersToMovementPointsToEscape.getValue(initialPlayersCount)
        } != null

    private fun isRoundOverBecauseOfEmptyDeck(): Boolean = playDeck.isEmpty()

    data class Builder(
        val player1: PlayerDescriptor,
        val player2: PlayerDescriptor,
        val deckType: DeckType = DeckType.STANDARD
    ) {

        val playerDescriptors: MutableList<PlayerDescriptor> = mutableListOf()

        init {
            withPlayer(player1)
            withPlayer(player2)
        }

        fun withPlayer(player: PlayerDescriptor) = apply { playerDescriptors.add(player) }

        fun build() = Game(this)
    }
}
