package main.kotlin.cardofthedead.game

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.cards.Event
import main.kotlin.cardofthedead.cards.StandardDeck
import main.kotlin.cardofthedead.cards.WayToPlayCard
import main.kotlin.cardofthedead.cards.Zombie
import main.kotlin.cardofthedead.players.Player
import java.util.stream.IntStream
import kotlin.random.Random

class Game(
    private val players: MutableList<Player>
) {

    private val dead: MutableList<Player> = mutableListOf()
    private val winners: MutableList<List<Player>> = mutableListOf()

    private val initialPlayersCount: Int = players.size

    private val deck: Deck = StandardDeck()
    private val discard: Deck = Deck()

    private var cardsToPlay: Int = 1 // because of Horde may be =2

    val playersToZombiesToBeSurrounded = mapOf(2 to 5, 3 to 4, 4 to 4, 5 to 3)
    private val playersToZombiesToBeEaten = mapOf(2 to 7, 3 to 6, 4 to 6, 5 to 5)
    private val playersToMovementPointsToEscape = mapOf(2 to 7, 3 to 6, 4 to 6, 5 to 5)

    private constructor(builder: Builder) : this(builder.players)

    init {
        winners.add(listOf(lastPlayerWentToShoppingMall()))
    }

    fun play() {
        IntStream.of(3).forEach { playRound() }
    }

    private fun playRound() {
        prepareForRound()

        var currentPlayer = getFirstPlayer()

        while (isRoundRunning()) {
            IntStream.of(cardsToPlay).forEach {
                playTurn(currentPlayer)
            }

            currentPlayer = getNextPlayer(currentPlayer)
        }
    }

    private fun playTurn(currentPlayer: Player) {
        when (val drawnCard = currentPlayer.drawTopCard(deck)) {
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
        }

        val decisionToPlayCardFromHand = currentPlayer.decideToPlayCardFromHand()
        if (decisionToPlayCardFromHand != WayToPlayCard.DO_NOT_PLAY) {
            val actionCardFromHand = currentPlayer.drawFromHand()

            if (decisionToPlayCardFromHand == WayToPlayCard.PLAY_AS_ACTION) {
                currentPlayer.play(actionCardFromHand)
            } else { // as movement points
                currentPlayer.addMovementPoints(actionCardFromHand)
                if (currentPlayer.getMovementPointsCount() >=
                    playersToMovementPointsToEscape.getValue(initialPlayersCount)
                ) {
                    return
                }
            }
        }
    }

    private fun prepareForRound() {
        players.addAll(dead)
        dead.clear()

        players.forEach { player ->
            player.returnCardsToDeck(player.hand, deck)
        }
        deck.merge(discard)

        deck.shuffle() // before initial dealing

        // initial dealing
        players.forEach { player ->
            player.pickCards(deck, 10)
            player.chooseSinglePointCards(3)
        }
        players.forEach { player ->
            player.returnCardsToDeck(player.theRestOfHand, deck)
        }

        deck.shuffle()
    }

    private fun getFirstPlayer(): Player =
        winners[winners.size - 1].elementAt(Random.nextInt(winners.size))

    private fun getNextPlayer(afterPlayer: Player): Player {
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

    private fun lastPlayerWentToShoppingMall(): Player =
        players.elementAt(Random.nextInt(players.size))

    private fun isRoundRunning(): Boolean =
        !isRoundOverBecauseOneAlivePlayerLeft() &&
                !isRoundOverBecauseEscapedPlayer() &&
                !isRoundOverBecauseOfEmptyDeck()

    private fun removePlayer(player: Player) {
        players.remove(player)
        dead.add(player)

        player.returnCardsToDeck(player.hand, deck)
        player.returnCardsToDeck(player.theRestOfHand, deck)
    }

    private fun isRoundOverBecauseOneAlivePlayerLeft(): Boolean =
        if (players.size == 1) {
            val winner = players[0]

            winner.addSurvivalPoints(5)

            winners.add(listOf(winner))

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
            players.forEach { player ->
                player.addSurvivalPoints(player.getMovementPointsCount())
            }

            winners.add(listOf(winner))

            true
        } else {
            false
        }
    }

    private fun isRoundOverBecauseOfEmptyDeck(): Boolean =
        if (deck.isEmpty()) {
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
