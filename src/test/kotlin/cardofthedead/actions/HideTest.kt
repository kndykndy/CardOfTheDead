package cardofthedead.actions

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class HideTest : StringSpec({

    "should give 1 out of 3 Zombie cards to next player" {
        // given

        val player1 = dummyPlayer().apply {
            chasedByZombies(LadZombie(), BrideZombie(), GrannyZombie())
        }

        val game = gameWithEmptyDeck(player1)

        val player2 = game.getNextPlayer(player1)

        // when
        player1.play(Hide().apply { gameContext = game })

        // then

        player1.getZombiesAroundCount() shouldBe 2 // Any 2 Zombies
        player2.getZombiesAroundCount() shouldBe 1 // Any Zombie
    }

    "should not give zombie cards if no Zombie cards" {
        // given

        val player1 = dummyPlayer().apply {
            chasedByZombies(Zombies(), `Zombies!!!`())
        }

        val game = gameWithEmptyDeck(player1)

        val player2 = game.getNextPlayer(player1)

        // when
        player1.play(Hide().apply { gameContext = game })

        // then

        player1.getZombiesAroundCount() shouldBe 5 // Zombies, Zombies!!!
        player2.getZombiesAroundCount() shouldBe 0
    }
})
