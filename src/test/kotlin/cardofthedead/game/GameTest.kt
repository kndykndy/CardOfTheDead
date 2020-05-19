package cardofthedead.game

import cardofthedead.TestUtils
import cardofthedead.TestUtils.wrapPlayersAsSpyKs
import io.kotest.core.spec.style.StringSpec

class GameTest : StringSpec({

    "should play game" {
        // given

        val game = TestUtils.gameWithEmptyDeck().wrapPlayersAsSpyKs()

        // when

        // then

    }
})
