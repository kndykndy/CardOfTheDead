package cardofthedead.cards.actions

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
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

        game.discardDeck.size() shouldBe 2 // Armored, Dynamite

        player1.getMovementPoints() shouldBe 0
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

        game.discardDeck.size() shouldBe 0

        player1.getMovementPoints() shouldBe 0
    }
})
