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
import kotlin.random.Random

class Game() {

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

    private constructor(builder: Builder) : this() {
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

        println("Starting a new Game of the Dead!")
        println("${playDeck.size()} cards in deck.")
        println("Tonight we're having ${players.size} players: ${players.joinToString { it.name }}.")

        val player = lastPlayerWentToShoppingMall()
        println("${player.name} was the last who went to shopping mall, he's starting!")
        winners.add(listOf(player))
    }

    fun play() {
        repeat(3) { i ->
            println("Starting round #${i + 1}")
            playRound()
        }

        announceWinners()
    }

    private fun announceWinners() {
        players.addAll(deadPlayers)

        println("Player scores: " +
                players.joinToString { it.name + " (${it.getSurvivalPoints()})" })

        val maxScore = players.map { it.getSurvivalPoints() }.max()
        val winners = players.groupBy { it.getSurvivalPoints() }[maxScore]!!

        if (winners.size == 1) {
            val winner = winners.first()
            println("The winner is ${winner.name + " (${winner.getSurvivalPoints()})"}")
        } else {
            println("The winners are " +
                    winners.joinToString { it.name + " (${it.getSurvivalPoints()})" }
            )
        }
    }

    private fun playRound() {
        prepareForRound()

        var currentPlayer = getFirstPlayer()
        println("${currentPlayer.name}'s starting!")

        while (isRoundRunning()) {
            playTurn(currentPlayer)

            currentPlayer = getNextPlayer(currentPlayer)
            println("${currentPlayer.name}'s turn now!")
        }
    }

    private fun playTurn(currentPlayer: Player) {
        repeat(cardsToPlay) {
            when (val drawnCard = currentPlayer.drawTopCard()) {
                is Action -> {
//                println("${currentPlayer.name} draws card to hand.")
                    println(
                        "DEBUG: ${currentPlayer.name} draws " +
                                "${drawnCard::class.simpleName + drawnCard.hashCode()} to hand."
                    )
                    currentPlayer.takeToHand(drawnCard)
                }
                is Zombie -> {
                    println(
                        "DEBUG: ${currentPlayer.name} draws " +
                                (drawnCard::class.simpleName + drawnCard.hashCode())
                    )
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
                    println(
                        "DEBUG: ${currentPlayer.name} draws " +
                                (drawnCard::class.simpleName + drawnCard.hashCode())
                    )
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
                if (currentPlayer.getMovementPointsCount() >=
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
        println("Preparing for round...")

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
            println("DEBUG: ${player.name} starts with ${player.hand}")
        }
        players.forEach { it.discardCandidatesCards() }
        playDeck.merge(discardDeck)

        playDeck.shuffle()
    }

    /**
     * No output!
     */
    private fun getFirstPlayer(): Player = winners.last().random()

    /**
     * No output!
     */
    internal fun getPrevPlayer(player: Player): Player {
        var result = players.last()

        val iterator = players.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next == player) {
                return result
            } else {
                result = next
            }
        }

        return players.last()
    }

    /**
     * No output!
     */
    internal fun getNextPlayer(afterPlayer: Player): Player {
        val iterator = players.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() == afterPlayer) {
                if (iterator.hasNext()) {
                    // println("DEBUG: Next player after ${afterPlayer.name} is ${nextPlayer.name}")
                    return iterator.next()
                }
            }
        }
//        println("DEBUG: Next player after ${afterPlayer.name} is ${players.first().name}")
        return players.first()
    }

    /**
     * No output!
     */
    internal fun getRandomPlayer(exceptForPlayer: Player): Player {
        while (true) {
            val randomPlayer = players.random()
            if (randomPlayer != exceptForPlayer) {
                return randomPlayer
            }
        }
    }

    private fun lastPlayerWentToShoppingMall(): Player =
        players.elementAt(Random.nextInt(players.size))

    private fun isRoundRunning(): Boolean =
        !isRoundOverBecauseOneAlivePlayerLeft() &&
                !isRoundOverBecauseEscapedPlayer() &&
                !isRoundOverBecauseOfEmptyDeck()

    private fun removePlayer(player: Player) {
        players.remove(player)
        deadPlayers.add(player)
    }

    private fun isRoundOverBecauseOneAlivePlayerLeft(): Boolean =
        if (players.size == 1) {
            val winner = players.first()
            winner.addSurvivalPoints(5)
            winners.add(listOf(winner))

            println("Oops... This round is over because ${winner.name} is the only player left alive!")

            true
        } else {
            false
        }

    private fun isRoundOverBecauseEscapedPlayer(): Boolean {
        val winner = players.firstOrNull { player ->
            player.getMovementPointsCount() >=
                    playersToMovementPointsToEscape.getValue(initialPlayersCount)
        }

        return if (winner != null) {
            players.forEach {
                it.addSurvivalPoints(it.getMovementPointsCount())
            }

            winners.add(listOf(winner))

            println("Oops... This round is over because ${winner.name} escaped!")

            true
        } else {
            false
        }
    }

    private fun isRoundOverBecauseOfEmptyDeck(): Boolean =
        if (playDeck.isEmpty()) {
            val maxMovementPoints =
                players.maxBy { it.getMovementPointsCount() }
                    ?.getMovementPointsCount()
            val winnersList = mutableListOf<Player>()
            players.forEach { player ->
                player.addSurvivalPoints(player.getMovementPointsCount())
                if (player.getMovementPointsCount() == maxMovementPoints) {
                    winnersList.add(player)
                }
            }

            winners.add(winnersList)

            println(
                "Oops... This round is over because there's no cards left in the deck! " +
                        "Winners are: ${winnersList.joinToString { it.name }}."
            )

            true
        } else {
            false
        }

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
