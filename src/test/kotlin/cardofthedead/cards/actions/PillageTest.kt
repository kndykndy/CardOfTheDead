package cardofthedead.cards.actions

import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.TestUtils.takeToHand
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PillageTest : StringSpec({

    "should take one card from every other player" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = game.getDummy().apply {
            takeToHand(Slugger(game), Hide(game), Lure(game))
        }
        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(Chainsaw(game), Dynamite(game))
        }
        val player3 = game.getNextPlayer(player2).apply {
            takeToHand(Bitten(game))
        }

        // when

        player1.play(Pillage(game))

        // then

        player1.hand.size() shouldBe 5 // Slugger, Hide, Lure, any of Chainsaw/Dynamite, Bitten
        player2.hand.size() shouldBe 1 // Any 1 of hand
        player3.hand.size() shouldBe 0
    }
})
