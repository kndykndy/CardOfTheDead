package cardofthedead.actions

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
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

        val player1 = spyk(dummyPlayer())
        every { player1.decideHowManyMovementCardsToDiscardForTripped() } returns 2

        val game = gameWithEmptyDeck(player1)

        val player2 = game.getNextPlayer(player1).apply {
            addMovementPoints(Armored(), Dynamite())
        }
        every { player1.choosePlayerToDiscardMovementCardsFromForTripped() } returns player2

        // when
        player1.play(Tripped().apply { gameContext = game })

        // then

        player1.getMovementPointsCount() shouldBe 0

        game.discardDeck.size() shouldBe 2 // Armored, Dynamite
    }

    "should not discard any cards if no escape cards" {
        // given

        val player1 = spyk(dummyPlayer())
        every { player1.decideHowManyMovementCardsToDiscardForTripped() } returns 2

        val game = gameWithEmptyDeck(player1)

        val player2 = game.getNextPlayer(player1)
        every { player1.choosePlayerToDiscardMovementCardsFromForTripped() } returns player2

        // when
        player1.play(Tripped().apply { gameContext = game })

        // then

        player1.getMovementPointsCount() shouldBe 0

        game.discardDeck.size() shouldBe 0
    }
})