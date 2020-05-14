package cardofthedead.cards.events

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FogTest : StringSpec({

    "should play Fog" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = game.getDummy().apply {
            takeToHand(Slugger(game), Hide(game))
            chasedByZombies(LadZombie(game), BrideZombie(game), Zombies(game))
        }

        game.getNextPlayer(player1).apply {
            takeToHand(Chainsaw(game))
            chasedByZombies(`Zombies!!!`(game))
        }

        // when

        player1.play(Fog(game))

        // then

        game.players.sumBy { it.getZombiesAroundCount() } shouldBe 7 // all zombies in the game
        game.players.sumBy { it.hand.size() } shouldBe 3 // Slugger, Hide, Chainsaw
    }
})
