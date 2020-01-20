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
    private val players: MutableSet<Player>
) {

    private val initialPlayersCount: Int = players.size

    private val deck: Deck = StandardDeck()
    private val discard: Deck = Deck()

    private var cardsToPlay: Int = 1 // because of Horde may be =2

    val playersToZombiesToBeSurrounded = mapOf(2 to 5, 3 to 4, 4 to 4, 5 to 3)
    private val playersToZombiesToBeEaten = mapOf(2 to 7, 3 to 6, 4 to 6, 5 to 5)
    private val playersToMovementPointsToEscape = mapOf(2 to 7, 3 to 6, 4 to 6, 5 to 5)

    private constructor(builder: Builder) : this(builder.players)

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

    private fun getFirstPlayer(): Player {
        return lastPlayerWentToShoppingMall() // for first round,
        // todo for second and third, the winner for previous round
        // todo winners set
    }

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

    private fun lastPlayerWentToShoppingMall(): Player {
        return players.elementAt(Random.nextInt(players.size))
    }

    private fun isRoundRunning(): Boolean {
        if (getAlivePlayersCount() == 1) {
            // todo alive player gets 5 Survival points
            return false
        }
        if (isPlayerWithEnoughPointsToEscape()) {
            // todo give all non eaten players survival points=their movement points
            return false
        }
        if (deck.isEmpty()) {
            // todo give all non eaten players survival points=their movement points
            return false
        }

        return true
    }

    private fun isPlayerWithEnoughPointsToEscape(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun removePlayer(player: Player) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getAlivePlayersCount(): Int {
        return players.size
    }

    class Builder {
        val players: MutableSet<Player> = mutableSetOf()

        fun withPlayer(player: Player) = apply { players.add(player) }

        fun build() = Game(this)
    }
}
