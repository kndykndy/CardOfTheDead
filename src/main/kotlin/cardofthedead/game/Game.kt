package cardofthedead.game

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.Deck
import cardofthedead.cards.Event
import cardofthedead.cards.WayToPlayCard
import cardofthedead.cards.Zombie
import cardofthedead.players.Player
import kotlin.random.Random

class Game(
    internal val players: MutableList<Player>
) {

    private val deadPlayers: MutableList<Player> = mutableListOf()
    private val winners: MutableList<List<Player>> = mutableListOf()

    internal val initialPlayersCount: Int = players.size

    lateinit var playDeck: Deck<Card>
    internal val discardDeck: Deck<Card> = Deck()

    /**
     * Because of Horde cards per round taken by each turn will be two.
     */
    internal var cardsToPlay: Int = 1

    val playersToZombiesToBeSurrounded = mapOf(2 to 5, 3 to 4, 4 to 4, 5 to 3)
    private val playersToZombiesToBeEaten = mapOf(2 to 7, 3 to 6, 4 to 6, 5 to 5)
    private val playersToMovementPointsToEscape = mapOf(2 to 7, 3 to 6, 4 to 6, 5 to 5)

    private constructor(builder: Builder) : this(builder.players) {
        this.playDeck = builder.deck

        println("Starting a new Game of the Dead!")

        playDeck.cards.forEach { it.gameContext = this }
        println("${playDeck.cards.size} cards in deck.")

        players.forEach { it.gameContext = this }
        println("Tonight we're having ${players.size} players: ${players.joinToString { it.name }}.")

        val player = lastPlayerWentToShoppingMall()
        println("${player.name} was the last who went to shopping mall!")
        winners.add(listOf(player))
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
        println("${currentPlayer.name}'s starting!")

        while (isRoundRunning()) {
            repeat(cardsToPlay) { playTurn(currentPlayer) }

            currentPlayer = getNextPlayer(currentPlayer)
            println("${currentPlayer.name}'s turn now!")
        }
    }

    private fun playTurn(currentPlayer: Player) {
        when (val drawnCard = currentPlayer.drawTopCard()) {
            is Action -> {
                println("${currentPlayer.name} draws card to hand.")
                currentPlayer.takeToHand(drawnCard)
            }
            is Zombie -> {
                println("${currentPlayer.name} draws ${drawnCard::class.simpleName}")
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
                println("${currentPlayer.name} draws ${drawnCard::class.simpleName}")
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

    /**
     * No output!
     */
    private fun getFirstPlayer(): Player =
        winners[winners.size - 1].elementAt(Random.nextInt(winners.size))

    /**
     * No output!
     */
    internal fun getPrevPlayer(player: Player): Player {
        var result = players.elementAt(players.size - 1)

        val iterator = players.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next == player) {
                return result
            } else {
                result = next
            }
        }

        return players.elementAt(players.size - 1)
    }

    /**
     * No output!
     */
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

    /**
     * No output!
     */
    internal fun getRandomPlayer(exceptForPlayer: Player): Player {
        val exceptForPlayerIdx = players.indexOf(exceptForPlayer)
        while (true) {
            val playerIdx = Random.nextInt(players.size)
            if (playerIdx != exceptForPlayerIdx) {
                return players[playerIdx]
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

    class Builder(
        player1: Player,
        player2: Player,
        deck: Deck<Card>
    ) {

        val players: MutableList<Player> = mutableListOf()
        var deck: Deck<Card>

        init {
            withPlayer(player1)
            withPlayer(player2)
            this.deck = deck
        }

        fun withPlayer(player: Player) = apply { players.add(player) }

        fun build() = Game(this)
    }
}
