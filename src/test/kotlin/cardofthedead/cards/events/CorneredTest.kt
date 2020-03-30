package cardofthedead.cards.events

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Dynamite
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CorneredTest : StringSpec({

    "should discard all escape cards" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getDummy().apply {
            addMovementPoints(Armored(game), Dynamite(game))
        }

        // when
        player.play(Cornered(game))

        // then

        player.getMovementPointsCount() shouldBe 0

        game.discardDeck.size() shouldBe 2 // Armored, Dynamite
    }

    "should not discard any cards if no escape cards" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getDummy()

        // when
        player.play(Cornered(game))

        // then

        player.getMovementPointsCount() shouldBe 0

        game.discardDeck.size() shouldBe 0
    }
})
