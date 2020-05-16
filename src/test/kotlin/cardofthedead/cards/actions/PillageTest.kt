package cardofthedead.cards.actions

import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.TestUtils.promotePlayersToSpies
import cardofthedead.TestUtils.takeToHand
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedPillage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every

class PillageTest : StringSpec({

    "should take one card from every other player" {
        // given

        val game = gameWithEmptyDeck().promotePlayersToSpies()

        val chainsaw = Chainsaw(game)
        val bitten = Bitten(game)

        val player1 = game.getFirstPlayer().apply {
            takeToHand(Slugger(game), Hide(game), Lure(game))
        }
        every { player1.throwDice(deck = any()) } returns 0
        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(chainsaw, Dynamite(game))
        }
        val player3 = game.getNextPlayer(player2).apply {
            takeToHand(bitten)
        }

        // when

        player1.play(Pillage(game))

        // then

        game.assertEvent(PlayedPillage(player1, listOf(chainsaw, bitten)))

        player1.hand.size() shouldBe 5 // Slugger, Hide, Lure, any of Chainsaw/Dynamite, Bitten
        player2.hand.size() shouldBe 1 // Any 1 of hand
        player3.hand.size() shouldBe 0
    }
})
