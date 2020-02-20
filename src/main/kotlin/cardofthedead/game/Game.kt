package main.kotlin.cardofthedead.game

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.cards.Event
import main.kotlin.cardofthedead.cards.StandardDeck
import main.kotlin.cardofthedead.cards.WayToPlayCard
import main.kotlin.cardofthedead.cards.Zombie
import main.kotlin.cardofthedead.players.Player
import kotlin.random.Random

class Game(
    internal val players: MutableList<Player>
) {

    private val deadPlayers: MutableList<Player> = mutableListOf()
    private val winners: MutableList<List<Player>> = mutableListOf()

    internal val initialPlayersCount: Int = players.size

    internal val playDeck: Deck<Card> = StandardDeck()
    internal val discardDeck: Deck<Card> = Deck()

    /**
     * Because of Horde cards per round taken by each turn will be two.
     */
    private var cardsToPlay: Int = 1

    val playersToZombiesToBeSurrounded = mapOf(2 to 5, 3 to 4, 4 to 4, 5 to 3)
    private val playersToZombiesToBeEaten = mapOf(2 to 7, 3 to 6, 4 to 6, 5 to 5)
    private val playersToMovementPointsToEscape = mapOf(2 to 7, 3 to 6, 4 to 6, 5 to 5)

    private constructor(builder: Builder) : this(builder.players)

    init {
        println("Starting a new Game of the Dead!")
        println()

        playDeck.cards.forEach { it.gameContext = this }
        println("${playDeck.cards.size} cards in deck.")
        println()

        players.forEach { it.gameContext = this }
        println("Tonight we're having ${players.size} players: ${players.joinToString { it.name }}.")

        winners.add(listOf(lastPlayerWentToShoppingMall()))

        println()
    }

    fun play() {
        repeat(3) { i ->
            println("Starting round #${i + 1}")
            playRound()
        }
    }

    private fun playRound() {
        prepareForRound()

        var currentPlayer = getFirstPlayer()

        while (isRoundRunning()) {
            repeat(cardsToPlay) { playTurn(currentPlayer) }

            currentPlayer = getNextPlayer(currentPlayer)
        }
    }

    private fun playTurn(currentPlayer: Player) {
        when (val drawnCard = currentPlayer.drawTopCard()) {
            is Action -> currentPlayer.takeToHand(drawnCard)
            is Zombie -> {
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
                currentPlayer.play(drawnCard)
                currentPlayer.discard(drawnCard)
            }
            null -> {
                return
            }
        }

        val decisionToPlayCardFromHand = currentPlayer.decideToPlayCardFromHand()
        if (decisionToPlayCardFromHand.isGonnaPlay()) {
            val actionCardFromHand: Card = decisionToPlayCardFromHand.card!!

            if (decisionToPlayCardFromHand.wayToPlayCard == WayToPlayCard.PLAY_AS_ACTION) {
                currentPlayer.play(actionCardFromHand)
            } else { // as movement points
                currentPlayer.addMovementPoints(actionCardFromHand as Action)
                if (currentPlayer.getMovementPointsCount() >=
                    playersToMovementPointsToEscape.getValue(initialPlayersCount)
                ) {
                    return
                }
            }
        }
    }

    private fun prepareForRound() {
        println("Preparing for round...")

        players.addAll(deadPlayers)
        deadPlayers.clear()

        players.forEach { it.discardAllCards() }
        playDeck.merge(discardDeck)

        playDeck.shuffle() // before initial dealing

        // initial dealing
        players.forEach {
            it.pickCards(10)
            it.chooseSinglePointCards(3)
        }
        players.forEach { it.discardCandidatesCards() }

        playDeck.shuffle()
    }

    private fun getFirstPlayer(): Player {
        val player = winners[winners.size - 1].elementAt(Random.nextInt(winners.size))

        println("${player.name}'s starting!")

        return player
    }

    internal fun getNextPlayer(afterPlayer: Player): Player {
        val iterator = players.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() == afterPlayer) {
                if (iterator.hasNext()) {
                    return iterator.next()
                }
            }
        }
        return players.elementAt(0)
    }

    internal fun getRandomPlayer(exceptForPlayer: Player): Player {
        val exceptForPlayerIdx = players.indexOf(exceptForPlayer)
        while (true) {
            val playerIdx = Random.nextInt(players.size)
            if (playerIdx != exceptForPlayerIdx) {
                return players[playerIdx]
            }
        }
    }

    private fun lastPlayerWentToShoppingMall(): Player {
        val player = players.elementAt(Random.nextInt(players.size))

        println("${player.name} was the last who went to shopping mall!")

        return player
    }

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
            val winner = players[0]
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
                players.maxBy { player ->
                    player.getMovementPointsCount()
                }!!.getMovementPointsCount()
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

    class Builder {
        val players: MutableList<Player> = mutableListOf()

        fun withPlayer(player: Player) = apply { players.add(player) }

        fun build() = Game(this)
    }
}
