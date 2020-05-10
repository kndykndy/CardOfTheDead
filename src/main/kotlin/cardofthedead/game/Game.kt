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
import cardofthedead.game.EventsFacade.Game.Amid.ChoseFirstPlayer
import cardofthedead.game.EventsFacade.Game.Amid.ChoseNextPlayer
import cardofthedead.game.EventsFacade.Game.Amid.DecidedNotToPlayFromHand
import cardofthedead.game.EventsFacade.Game.Amid.DecidedToPlayFromHand
import cardofthedead.game.EventsFacade.Game.Amid.DecidedToPlayFromHandAsMp
import cardofthedead.game.EventsFacade.Game.Amid.Died
import cardofthedead.game.EventsFacade.Game.Amid.DrewAction
import cardofthedead.game.EventsFacade.Game.Amid.DrewEvent
import cardofthedead.game.EventsFacade.Game.Amid.DrewNoCard
import cardofthedead.game.EventsFacade.Game.Amid.DrewNoCardReason
import cardofthedead.game.EventsFacade.Game.Amid.DrewZombie
import cardofthedead.game.EventsFacade.Game.Amid.StartedNewRound
import cardofthedead.game.EventsFacade.Game.Amid.WonRoundCauseDeckOver
import cardofthedead.game.EventsFacade.Game.Amid.WonRoundCauseEscaped
import cardofthedead.game.EventsFacade.Game.Amid.WonRoundCauseOneAlive
import cardofthedead.game.EventsFacade.Game.Around.AnnouncedGameWinners
import cardofthedead.game.EventsFacade.Game.Around.StartedNewGame
import cardofthedead.players.EasyPlayer
import cardofthedead.players.HardPlayer
import cardofthedead.players.Level
import cardofthedead.players.Player
import cardofthedead.players.PlayerDescriptor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.random.Random

class Game private constructor(builder: Builder) {

    /**
     * Events superqueue, combines all the subqueues from players and game queue.
     */
    private var eventQueue: Observable<EventsFacade.Event>

    /**
     * Game queue.
     */
    private val events: PublishSubject<EventsFacade.Event> = PublishSubject.create()

    internal val players: MutableList<Player> = mutableListOf()

    private val deadPlayers: MutableList<Player> = mutableListOf()
    private val winners: MutableList<List<Player>> = mutableListOf()

    private var initialPlayersCount: Int = 0

    internal val playDeck: Deck<Card> = EmptyDeck(this)
    internal val discardDeck: Deck<Card> = EmptyDeck(this)

    /**
     * Because of Horde cards per round taken by each turn will be two.
     */
    internal var cardsToPlay: Int = 1

    private val playersToZombiesToBeSurrounded = mapOf(2 to 5, 3 to 4, 4 to 4, 5 to 3)
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

        eventQueue = Observable.merge(players.map { it.getEvents() }.plus(events))

        winners.add(listOf(lastPlayerWentToShoppingMall()))
    }

    fun getEventQueue(): Observable<EventsFacade.Event> = eventQueue

    fun publishEvent(event: EventsFacade.Event) = events.onNext(event)

    fun getZombiesCountToBeSurrounded() =
        playersToZombiesToBeSurrounded.getValue(initialPlayersCount)

    private fun getZombiesCountToBeEaten() =
        playersToZombiesToBeEaten.getValue(initialPlayersCount)

    private fun getMovementPointsToEscape() =
        playersToMovementPointsToEscape.getValue(initialPlayersCount)

    fun play() {
        publishEvent(
            StartedNewGame(
                playDeck.size(),
                players.size,
                players,
                winners.first().first()
            )
        )

        repeat(3) { i ->
            publishEvent(StartedNewRound(i + 1))
            playRound()
        }

        announceGameWinners()
    }

    private fun playRound() {
        prepareForRound()

        var currentPlayer = winners.last().random()
        publishEvent(ChoseFirstPlayer(currentPlayer))

        while (isRoundRunning()) {
            playTurn(currentPlayer)

            currentPlayer = getNextPlayer(currentPlayer)
            publishEvent(ChoseNextPlayer(currentPlayer))
        }

        announceRoundWinners()
    }

    private fun announceRoundWinners() {
        val winnersList = when {
            isOnePlayerAliveLeft() -> {
                val winner = players.first().also {
                    it.addSurvivalPoints(5)
                }

                publishEvent(WonRoundCauseOneAlive(winner, players.plus(deadPlayers)))

                listOf(winner)
            }
            isAnyPlayerEscaped() -> {
                val winner = players
                    .map { it.addSurvivalPoints(it.getMovementPoints()); it }
                    .first { it.getMovementPoints() >= getMovementPointsToEscape() }

                publishEvent(WonRoundCauseEscaped(winner, players.plus(deadPlayers)))

                listOf(winner)
            }
            isPlayDeckEmpty() -> {
                val maxMovementPoints = players.map { it.getMovementPoints() }.max()

                val winners = players
                    .map { it.addSurvivalPoints(it.getMovementPoints()); it }
                    .filter { it.getMovementPoints() == maxMovementPoints }

                publishEvent(WonRoundCauseDeckOver(winners, players.plus(deadPlayers)))

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

        publishEvent(AnnouncedGameWinners(players, winners))
    }

    private fun playTurn(player: Player) {
        repeat(cardsToPlay) {
            when (val drawnCard = player.drawTopCard()) {
                is Action -> drewAction(player, drawnCard)
                is Zombie -> if (drewZombie(player, drawnCard)) return
                is Event -> drewEvent(player, drawnCard)
                null -> drewNoCard(player)
            }
        }

        playCardFromHand(player)
    }

    private fun drewAction(player: Player, action: Action) {
        player.takeToHand(action)

        publishEvent(DrewAction(player, action))
    }

    private fun drewZombie(player: Player, zombie: Zombie): Boolean {
        player.chasedByZombie(zombie)

        publishEvent(DrewZombie(player, zombie))

        val isEaten = player.getZombiesAroundCount() >= getZombiesCountToBeEaten()
        if (isEaten) {
            player.die()
            removePlayer(player)

            publishEvent(Died(player))
        }

        return isEaten
    }

    private fun drewEvent(player: Player, event: Event) {
        publishEvent(DrewEvent(player, event))

        player.play(event)
        player.discard(event)
    }

    private fun drewNoCard(player: Player) {
        publishEvent(
            DrewNoCard(
                player,
                if (isPlayDeckEmpty()) DrewNoCardReason.DeckIsEmpty
                else DrewNoCardReason.DecidedNotToDraw
            )
        )
    }

    private fun playCardFromHand(player: Player) {
        val decisionToPlayCardFromHand = player.decideToPlayCardFromHand()
        if (decisionToPlayCardFromHand.isGonnaPlay()) {
            val actionCardFromHand = decisionToPlayCardFromHand.card ?: return

            if (WayToPlayCard.PLAY_AS_ACTION == decisionToPlayCardFromHand.wayToPlayCard) {
                publishEvent(
                    DecidedToPlayFromHand(
                        player, actionCardFromHand
                    )
                )

                player.play(actionCardFromHand)
                player.discard(actionCardFromHand)
            } else { // as movement points
                player.addMovementPoints(actionCardFromHand as Action)

                publishEvent(
                    DecidedToPlayFromHandAsMp(
                        player, actionCardFromHand
                    )
                )
            }
        } else {
            publishEvent(DecidedNotToPlayFromHand(player))
        }
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
            player.chooseSinglePointCardsFromCandidates(3)
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
        !isOnePlayerAliveLeft() && !isAnyPlayerEscaped() && !isPlayDeckEmpty()

    private fun isOnePlayerAliveLeft(): Boolean = players.size == 1

    private fun isAnyPlayerEscaped(): Boolean =
        players.firstOrNull { it.getMovementPoints() >= getMovementPointsToEscape() } != null

    private fun isPlayDeckEmpty(): Boolean = playDeck.isEmpty()

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
