package cardofthedead.cards.actions

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.TestUtils.wrapPlayersAsSpyKs
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedTripped
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every

class TrippedTest : StringSpec({

    "should discard 2 escape cards" {
        // given

        val game = gameWithEmptyDeck().wrapPlayersAsSpyKs()

        val armored = Armored(game)
        val dynamite = Dynamite(game)

        val player1 = game.getFirstPlayer()
        val player2 = game.getNextPlayer(player1).apply {
            addMovementPoints(armored, dynamite)
        }

        every { player1.decideHowManyMovementCardsToDiscardForTripped() } returns 2
        every { player1.choosePlayerToDiscardMovementCardsFromForTripped() } returns player2

        // when

        player1.play(Tripped(game))

        // then

        game.discardDeck.cards shouldContainExactly listOf(dynamite, armored)
        game.assertEvent(PlayedTripped(player1, listOf(dynamite, armored), player2))

        player1.getMovementPoints() shouldBe 0
    }

    "should not discard any cards if no escape cards" {
        // given

        val game = gameWithEmptyDeck().wrapPlayersAsSpyKs()

        val player1 = game.getFirstPlayer()
        val player2 = game.getNextPlayer(player1)

        every { player1.decideHowManyMovementCardsToDiscardForTripped() } returns 2
        every { player1.choosePlayerToDiscardMovementCardsFromForTripped() } returns player2

        // when

        player1.play(Tripped(game))

        // then

        game.discardDeck.size() shouldBe 0
        game.assertEvent(PlayedTripped(player1, emptyList(), player2))

        player1.getMovementPoints() shouldBe 0
    }
})
