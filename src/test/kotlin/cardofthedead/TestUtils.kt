package cardofthedead

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.cards.Zombie
import cardofthedead.decks.Deck
import cardofthedead.decks.DeckType
import cardofthedead.game.Game
import cardofthedead.players.Player
import cardofthedead.players.PlayerDescriptor
import cardofthedead.players.Sex

object TestUtils {

    // Game

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

    fun gameWithEmptyDeck() = gameWithDeck(donaldTrumpDescriptor(), DeckType.EMPTY)
    fun gameWithStandardDeck() = gameWithDeck(donaldTrumpDescriptor(), DeckType.STANDARD)

    fun Game.getDummy() = this.players.last()

    // Players

    private fun donaldTrumpDescriptor() = PlayerDescriptor("Donald Trump")

    fun testPlayer(gameContext: Game): Player =
        object : Player(gameContext, "Darth Vader", Sex.MALE) {
            override fun chooseSinglePointCards(n: Int) {}
            override fun decideToPlayCardFromHand() = PlayCardDecision.doNotPlay()
            override fun chooseWorstCandidateForBarricade(): Card? = null
            override fun chooseWorstMovementCardForDynamite(): Card? = null
            override fun decideToDrawNoCardsNextTurnForHide() {}
            override fun choosePlayerToGiveZombieToForLure(): Player = this
            override fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean = false
            override fun choosePlayerToTakeCardFromForSlugger(): Player = this
            override fun choosePlayerToDiscardMovementCardsFromForTripped(): Player = this
            override fun decideHowManyMovementCardsToDiscardForTripped(): Int = 0
        }

    fun Player.takeToHand(vararg listOfCards: Card) =
        listOfCards.forEach { this.takeToHand(it) }

    fun Player.chasedByZombies(vararg listOfZombies: Zombie) =
        listOfZombies.forEach { this.chasedByZombie(it) }

    fun Player.addMovementPoints(vararg listOfActions: Action) =
        listOfActions.forEach { this.addMovementPoints(it) }

    // Cards

    fun <T : Card> Deck<T>.addCards(vararg listOfCards: T) =
        listOfCards.forEach { this.addCard(it) }
}