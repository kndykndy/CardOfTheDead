package cardofthedead.cards.actions

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk

class LureTest : StringSpec({

    "should give 1 out of 3 Zombie cards to any player" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = spyk(game.getDummy()).apply {
            chasedByZombies(LadZombie(game), BrideZombie(game), GrannyZombie(game))
        }

        val player2 = game.getNextPlayer(player1)
        every { player1.choosePlayerToGiveZombieToForLure() } returns player2

        // when
        player1.play(Lure(game))

        // then

        player1.getZombiesAroundCount() shouldBe 2 // Any 2 Zombies
        player2.getZombiesAroundCount() shouldBe 1 // Any Zombie
    }

    "should not give zombie cards if no Zombie cards" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = spyk(game.getDummy()).apply {
            chasedByZombies(Zombies(game), `Zombies!!!`(game))
        }

        val player2 = game.getNextPlayer(player1)
        every { player1.choosePlayerToGiveZombieToForLure() } returns player2

        // when
        player1.play(Lure(game))

        // then

        player1.getZombiesAroundCount() shouldBe 5 // Zombies, Zombies!!!
        player2.getZombiesAroundCount() shouldBe 0
    }
})
