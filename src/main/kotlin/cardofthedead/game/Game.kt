package main.kotlin.cardofthedead.game

import main.kotlin.cardofthedead.cards.Action
import main.kotlin.cardofthedead.cards.Alert
import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.cards.StandardDeck
import main.kotlin.cardofthedead.cards.Zombie
import main.kotlin.cardofthedead.players.Player
import java.util.stream.IntStream
import kotlin.random.Random

class Game() {

    private val players: Set<Player>

    private val deck: Deck = StandardDeck()
    private val graveyard: Deck = Deck()

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

        val firstPlayerIdx = getFirstPlayerIdx()
        var currentPLayerIdx = firstPlayerIdx

        while (isRoundRunning()) {
            val player = players.elementAt(currentPLayerIdx)

            val drawnCard = player.drawTopCard(deck)
            when (drawnCard) {
                is Action -> {
                    player.take(drawnCard)
                }
                is Zombie -> {
                    player.surroundBy(drawnCard)
                }
                is Alert -> {
                    player.play(drawnCard)
                    player.
                }
            }

            player.playLastCard()
            player.playCardFromHand()

            currentPLayerIdx++
            if (currentPLayerIdx > players.size) {
                currentPLayerIdx = 0
            }
        }
    }

    private fun prepareForRound() {
        players.forEach { player ->
            player.returnCardsToDeck(player.hand, deck)
        }
        deck.merge(graveyard)

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

    private fun getFirstPlayerIdx(): Int {
        return Random.nextInt(players.size) // for first round,
        // todo for second and third, the winner for previous round
    }

    private fun isRoundRunning(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}