package cardofthedead.events

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.events.Fog
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FogTest : StringSpec({

    "should play Fog" {
        // given

        val player1 = dummyPlayer().apply {
            takeToHand(Slugger(), Hide())
            chasedByZombies(LadZombie(), BrideZombie(), Zombies())
        }

        val game = gameWithEmptyDeck(player1)
        game.getNextPlayer(player1).apply {
            takeToHand(Chainsaw())
            chasedByZombies(`Zombies!!!`())
        }

        // when
        player1.play(Fog().apply { gameContext = game })

        // then

        game.players.sumBy { it.getZombiesAroundCount() } shouldBe 7 // all zombies in the game
        game.players.sumBy { it.hand.size() } shouldBe 3 // Slugger, Hide, Chainsaw
    }
})