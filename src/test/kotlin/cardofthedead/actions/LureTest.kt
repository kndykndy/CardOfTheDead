package cardofthedead.actions

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithDeck
import cardofthedead.cards.EmptyDeck
import cardofthedead.cards.Zombie
import cardofthedead.cards.actions.Lure
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

        val deck = EmptyDeck()

        val player1 = spyk(dummyPlayer()).apply {
            chasedByZombies(
                deck.addCard(LadZombie()) as Zombie,
                deck.addCard(BrideZombie()) as Zombie,
                deck.addCard(GrannyZombie()) as Zombie
            )
        }

        val game = gameWithDeck(player1, deck)

        val player2 = game.getNextPlayer(player1)
        every { player1.choosePlayerToGiveZombieToForLure() } returns player2

        // when
        player1.play(Lure().apply { gameContext = game })

        // then

        player1.getZombiesAroundCount() shouldBe 2 // Any 2 Zombies
        player2.getZombiesAroundCount() shouldBe 1 // Any Zombie
    }

    "should not give zombie cards if no Zombie cards" {
        // given

        val deck = EmptyDeck()

        val player1 = spyk(dummyPlayer()).apply {
            chasedByZombies(
                deck.addCard(Zombies()) as Zombie,
                deck.addCard(`Zombies!!!`()) as Zombie
            )
        }

        val game = gameWithDeck(player1, deck)

        val player2 = game.getNextPlayer(player1)
        every { player1.choosePlayerToGiveZombieToForLure() } returns player2

        // when
        player1.play(Lure().apply { gameContext = game })

        // then

        player1.getZombiesAroundCount() shouldBe 5 // Zombies, Zombies!!!
        player2.getZombiesAroundCount() shouldBe 0
    }
})
