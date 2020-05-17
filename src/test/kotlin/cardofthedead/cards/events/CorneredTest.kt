package cardofthedead.cards.events

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Dynamite
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedCornered
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CorneredTest : StringSpec({

    "should discard all escape cards" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getFirstPlayer().apply {
            addMovementPoints(Armored(game), Dynamite(game))
        }

        // when

        player.play(Cornered(game))

        // then

        game.discardDeck.size() shouldBe 2 // Armored, Dynamite
        game.assertEvent(PlayedCornered(player))

        player.getMovementPoints() shouldBe 0
    }

    "should not discard any cards if no escape cards" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getFirstPlayer()

        // when

        player.play(Cornered(game))

        // then

        game.discardDeck.size() shouldBe 0
        game.assertEvent(PlayedCornered(player))

        player.getMovementPoints() shouldBe 0
    }
})
