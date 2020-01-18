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

class Game() {

    private val players: MutableSet<Player>

    private val deck: Deck = StandardDeck()
    private val discard: Deck = Deck()

    init {
        // todo get amount of players from args
        players = linkedSetOf(
            Player("Женя"),
            Player("Дима"),
            Player("Настя")
        )
    }

    fun play() {
        IntStream.of(3).forEach { playRound() }
    }

    private fun playRound() {
        prepareForRound()

        var currentPlayer = getFirstPlayer()

        while (isRoundRunning()) {
            val drawnCard = currentPlayer.drawTopCard(deck)
            if(drawnCard==null){ // end of deck

            }

            when (drawnCard) {
                is Action -> currentPlayer.takeToHand(drawnCard)
                is Zombie -> currentPlayer.chasedByZombie(drawnCard)
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
                }
            }

            currentPlayer = getNextPlayer(currentPlayer)
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
    }

    private fun getNextPlayer(afterPlayer: Player): Player {
        val iterator = players.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() == afterPlayer) {
                return if (iterator.hasNext()) iterator.next() else players.elementAt(0)
            }
        }
        return null
    }

    private fun lastPlayerWentToShoppingMall(): Player {
        return players.elementAt(Random.nextInt(players.size))
    }

    private fun isRoundRunning(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}