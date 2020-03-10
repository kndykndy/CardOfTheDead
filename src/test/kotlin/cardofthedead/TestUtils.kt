package cardofthedead

import cardofthedead.cards.Card
import cardofthedead.cards.Deck
import cardofthedead.cards.EmptyDeck
import cardofthedead.cards.StandardDeck
import cardofthedead.game.Game
import cardofthedead.players.Player

object TestUtils {

    private fun gameWithDeck(player: Player, deck: Deck<Card>) =
        Game.Builder(Player.of("John Doe"), Player.of("Jane Doe"), deck)
            .withPlayer(player)
            .build()

    fun gameWithEmptyDeck(player: Player) = gameWithDeck(player, EmptyDeck())
    fun gameWithStandardDeck(player: Player) = gameWithDeck(player, StandardDeck())

    fun dummyPlayer() = Player.of("Donald Trump")

    fun Player.takeToHand(vararg listOfCards: Card) =
        listOfCards.forEach { this.takeToHand(it) }

    fun Player.addCardsToCandidates(vararg listOfCards: Card) =
        listOfCards.forEach { this.candidatesToHand.addCard(it) }

    fun <T : Card> Deck<T>.addCards(vararg listOfCards: T) =
        listOfCards.forEach { this.addCard(it) }
}
