package main.kotlin.cardofthedead

import main.kotlin.cardofthedead.cards.Deck
import main.kotlin.cardofthedead.players.Player
import java.util.stream.IntStream
import kotlin.random.Random

fun main(args: Array<String>) {
    // todo get amount of players from args
    val players = linkedSetOf(
        Player("Женя"),
        Player("Дима"),
        Player("Настя")
    )

    val deck = Deck()
    deck.shuffle() // before initial dealing

    val graveyard = Deck()

    // initial dealing
    players.forEach { player ->
        player.pickCards(deck, 10)
        player.chooseSinglePointCards(3)
    }
    players.forEach { player ->
        player.returnCardsToDeck(player.theRestOfTempCards, deck)
    }

    IntStream.of(3).forEach {

        deck.shuffle()

        val firstPlayerIdx = getFirstPlayerIdx()
        var currentPLayerIdx = firstPlayerIdx

        while (isRoundRunning()) {
            val player = players.elementAt(currentPLayerIdx)
            player.drawCard(deck)
            player.playLastCard()
            player.playCardFromHand()

            currentPLayerIdx++
            if (currentPLayerIdx > players.size) {
                currentPLayerIdx = 0
            }
        }

        players.forEach { player ->
            player.returnCardsToDeck(player.hand, deck)
        }
        deck.merge(graveyard)
    }
}

fun getFirstPlayerIdx(): Int {
    return Random.nextInt(players.size) // for first round,
    // for second and third, the winner
}

fun isRoundRunning(): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
