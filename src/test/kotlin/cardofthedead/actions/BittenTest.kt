package cardofthedead.actions

import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.cards.actions.Bitten
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class BittenTest : StringSpec({

    "should throw exception if playing Bitten" {
        // given

        val player = dummyPlayer()

        gameWithEmptyDeck(player)

        // when
        val exception = shouldThrow<IllegalStateException> {
            player.play(Bitten())
        }

        // then
        exception.message shouldBe "Bitten cannot be played directly."
    }
})
