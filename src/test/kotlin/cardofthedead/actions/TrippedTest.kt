package cardofthedead.actions

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Tripped
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk

class TrippedTest : StringSpec({

    "should discard 2 escape cards" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = spyk(game.getDummy())
        every { player1.decideHowManyMovementCardsToDiscardForTripped() } returns 2

        val player2 = game.getNextPlayer(player1).apply {
            addMovementPoints(Armored(game), Dynamite(game))
        }
        every { player1.choosePlayerToDiscardMovementCardsFromForTripped() } returns player2

        // when
        player1.play(Tripped(game))

        // then

        player1.getMovementPointsCount() shouldBe 0

        game.discardDeck.size() shouldBe 2 // Armored, Dynamite
    }

    "should not discard any cards if no escape cards" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = spyk(game.getDummy())
        every { player1.decideHowManyMovementCardsToDiscardForTripped() } returns 2

        val player2 = game.getNextPlayer(player1)
        every { player1.choosePlayerToDiscardMovementCardsFromForTripped() } returns player2

        // when
        player1.play(Tripped(game))

        // then

        player1.getMovementPointsCount() shouldBe 0

        game.discardDeck.size() shouldBe 0
    }
})
