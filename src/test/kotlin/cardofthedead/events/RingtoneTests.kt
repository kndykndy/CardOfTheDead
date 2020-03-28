package cardofthedead.events

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.cards.events.Ringtone
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RingtoneTests : StringSpec({

    "should take a single zombie from all players" {
        // given

        val player1 = dummyPlayer().apply {
            chasedByZombie(LadZombie())
        }

        val game = gameWithEmptyDeck(player1)

        val player2 = game.getNextPlayer(player1).apply {
            chasedByZombies(GrannyZombie(), Zombies())
        }

        val player3 = game.getNextPlayer(player2).apply {
            chasedByZombies(`Zombies!!!`(), BrideZombie())
        }

        // when
        player1.play(Ringtone().apply { gameContext = game })

        // then

        player1.getZombiesAroundCount() shouldBe 3 // LadZombie, BrideZombie, GrannyZombie
        player2.getZombiesAroundCount() shouldBe 2 // Zombies
        player3.getZombiesAroundCount() shouldBe 3 // Zombies!!!
    }
})
