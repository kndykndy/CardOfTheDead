package cardofthedead.cards.actions

import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class BittenTest : StringSpec({

    "should throw exception if played" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getDummy()

        // when
        val exception = shouldThrow<IllegalStateException> {
            player.play(Bitten(game))
        }

        // then
        exception.message shouldBe "Bitten cannot be played directly."
    }
})
