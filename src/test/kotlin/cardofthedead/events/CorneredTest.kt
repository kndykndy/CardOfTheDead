package cardofthedead.events

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.events.Cornered
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CorneredTest : StringSpec({

    "should discard all escape cards" {
        // given

        val player = dummyPlayer().apply {
            addMovementPoints(Armored(), Dynamite())
        }

        val game = gameWithEmptyDeck(player)

        // when
        player.play(Cornered().apply { gameContext = game })

        // then

        player.getMovementPointsCount() shouldBe 0

        game.discardDeck.size() shouldBe 2 // Armored, Dynamite
    }

    "should not discard any cards if no escape cards" {
        // given

        val player = dummyPlayer()

        val game = gameWithEmptyDeck(player)

        // when
        player.play(Cornered().apply { gameContext = game })

        // then

        player.getMovementPointsCount() shouldBe 0

        game.discardDeck.size() shouldBe 0
    }
})
