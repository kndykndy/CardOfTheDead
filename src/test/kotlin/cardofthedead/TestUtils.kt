package cardofthedead

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.Deck
import cardofthedead.cards.DeckType
import cardofthedead.cards.Zombie
import cardofthedead.game.Game
import cardofthedead.players.Player
import cardofthedead.players.PlayerDescriptor

object TestUtils {

    private fun gameWithDeck(
        playerDescriptor: PlayerDescriptor,
        deckType: DeckType
    ) =
        Game.Builder(
            PlayerDescriptor("John Doe"),
            PlayerDescriptor("Jane Doe"),
            deckType
        )
            .withPlayer(playerDescriptor)
            .build()

    fun gameWithEmptyDeck() = gameWithDeck(dummyPlayer(), DeckType.EMPTY)
    fun gameWithStandardDeck() = gameWithDeck(dummyPlayer(), DeckType.STANDARD)

    private fun dummyPlayer() = PlayerDescriptor("Donald Trump")

    fun Game.getDummy() = this.players.last()

    fun Player.takeToHand(vararg listOfCards: Card) =
        listOfCards.forEach { this.takeToHand(it) }

    fun Player.addCardsToCandidates(vararg listOfCards: Card) =
        listOfCards.forEach { this.candidatesToHand.addCard(it) }

    fun Player.chasedByZombies(vararg listOfZombies: Zombie) =
        listOfZombies.forEach { this.chasedByZombie(it) }

    fun Player.addMovementPoints(vararg listOfActions: Action) =
        listOfActions.forEach { this.addMovementPoints(it) }

    fun <T : Card> Deck<T>.addCards(vararg listOfCards: T) =
        listOfCards.forEach { this.addCard(it) }
}
