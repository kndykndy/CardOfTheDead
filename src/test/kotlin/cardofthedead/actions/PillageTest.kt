package cardofthedead.actions

import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Lure
import cardofthedead.cards.actions.Pillage
import cardofthedead.cards.actions.Slugger
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PillageTest : StringSpec({

    "should take one card from every other player" {
        // given

        val player1 = dummyPlayer().apply {
            takeToHand(Slugger(), Hide(), Lure())
        }

        val game = gameWithEmptyDeck(player1)

        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(Chainsaw(), Dynamite())
        }
        val player3 = game.getNextPlayer(player2).apply {
            takeToHand(Bitten())
        }

        // when
        player1.play(Pillage().apply { gameContext = game })

        // then

        player1.hand.size() shouldBe 5 // Slugger, Hide, Lure, any of Chainsaw/Dynamite, Bitten
        player2.hand.size() shouldBe 1 // Any 1 of hand
        player3.hand.size() shouldBe 0
    }
})
