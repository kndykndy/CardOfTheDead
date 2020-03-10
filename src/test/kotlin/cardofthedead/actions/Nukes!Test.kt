package cardofthedead.actions

import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithStandardDeck
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.actions.`Nukes!`
import io.kotest.core.spec.style.StringSpec

@Suppress("ClassName")
class `Nukes!Test` : StringSpec({

    "should play Nukes!" {
        // given

        val player = dummyPlayer()
        player.takeToHand(Slugger(), Hide())

        val game = gameWithStandardDeck(player)

        val player1 = game.getNextPlayer(player)
        val player2 = game.getNextPlayer(player1)

        // when
        player.play(`Nukes!`())

        // then
        val p = 1
    }
})
