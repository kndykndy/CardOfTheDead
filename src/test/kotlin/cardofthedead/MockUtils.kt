package cardofthedead

import cardofthedead.cards.Card
import cardofthedead.cards.Deck
import cardofthedead.cards.EmptyDeck
import cardofthedead.cards.StandardDeck
import cardofthedead.game.Game
import cardofthedead.players.Player

fun gameWithEmptyDeck(playerUnderTest: Player) =
    Game.Builder(Player.of("John Doe"), Player.of("Jane Doe"), EmptyDeck())
        .withPlayer(playerUnderTest)
        .build()

fun gameWithStandardDeck(playerUnderTest: Player) =
    Game.Builder(Player.of("John Doe"), Player.of("Jane Doe"), StandardDeck())
        .withPlayer(playerUnderTest)
        .build()

fun dummyPlayer() = Player.of("Donald Trump")

fun <T : Card> Deck<T>.addCards(vararg listOfCards: T) =
    listOfCards.forEach { this.addCard(it) }

fun Player.addCardsToCandidates(listOfCards: List<Card>) =
    listOfCards.forEach { this.candidatesToHand.addCard(it) }
