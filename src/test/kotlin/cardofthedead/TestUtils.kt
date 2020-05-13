package cardofthedead

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.cards.Zombie
import cardofthedead.decks.Deck
import cardofthedead.decks.DeckType
import cardofthedead.game.EventsFacade
import cardofthedead.game.Game
import cardofthedead.players.EasyPlayer
import cardofthedead.players.HardPlayer
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
            PlayerDescriptor("Luke Skywalker"),
            PlayerDescriptor("Obi Van Kenobi"),
            deckType
        )
            .withPlayer(playerDescriptor)
            .build()

    fun gameWithEmptyDeck() = gameWithDeck(PlayerDescriptor("C3PO"), DeckType.EMPTY)
    fun gameWithStandardDeck() = gameWithDeck(PlayerDescriptor("C3PO"), DeckType.STANDARD)

    fun Game.getDummy() = this.players.last()

    fun Game.assertEvent(event: EventsFacade.Event) {
        val observer = this.getEventQueueTestObserver()
        observer.assertNoErrors()
        observer.assertValueCount(1)
        observer.assertValueAt(0) { it == event }
    }

    // Players

    fun testPlayer(game: Game): Player =
        object : Player(game, "R2D2", Sex.MALE) {
            override fun chooseSinglePointCardsFromCandidates(n: Int) {}
            override fun decideToPlayCardFromHand() = PlayCardDecision.doNotPlay()
            override fun chooseWorstCandidateForBarricade(): Card? = null
            override fun chooseWorstMovementCardForDynamite(): Action? = null
            override fun decideToDrawNoCardsNextTurnForHide(): Boolean = false
            override fun choosePlayerToGiveZombieToForLure(): Player = this
            override fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean = false
            override fun choosePlayerToTakeCardFromForSlugger(): Player = this
            override fun choosePlayerToDiscardMovementCardsFromForTripped(): Player = this
            override fun decideHowManyMovementCardsToDiscardForTripped(): Int = 0
        }

    fun testEasyPlayer(game: Game): EasyPlayer = EasyPlayer(game, "Kylo Ren", Sex.FEMALE)
    fun testHardPlayer(game: Game): HardPlayer = HardPlayer(game, "Darth Vader", Sex.MALE)

    fun Player.takeToHand(vararg listOfCards: Card) =
        listOfCards.forEach { this.takeToHand(it) }

    fun Player.takeToCandidates(vararg listOfCards: Card) =
        listOfCards.forEach { this.candidatesToHand.addCard(it) }

    fun Player.chasedByZombies(vararg listOfZombies: Zombie) =
        listOfZombies.forEach { this.chasedByZombie(it) }

    fun Player.addMovementPoints(vararg listOfActions: Action) =
        listOfActions.forEach { this.addMovementPoints(it) }

    // Cards

    fun <T : Card> Deck<T>.addCards(vararg listOfCards: T) =
        listOfCards.forEach { this.addCard(it) }
}
