package cardofthedead.cards.actions

import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.TestUtils.promotePlayersToSpies
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedHide
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every

class HideTest : StringSpec({

    "should give 1 out of 3 Zombie cards to next player" {
        // given

        val game = gameWithEmptyDeck().promotePlayersToSpies()

        val player1 = game.getDummy().apply {
            chasedByZombies(LadZombie(game), BrideZombie(game), GrannyZombie(game))
        }
        every { player1.decideToDrawNoCardsNextTurnForHide() } returns true

        val player2 = game.getNextPlayer(player1)

        // when

        player1.play(Hide(game))

        // then

        game.assertEvent(PlayedHide(player1, player2.zombiesAround.cards.first(), player2, true))

        player1.getZombiesAroundCount() shouldBe 2 // Any 2 Zombies
        player2.getZombiesAroundCount() shouldBe 1 // Any Zombie
    }

    "should not give zombie cards if no Zombie cards" {
        // given

        val game = gameWithEmptyDeck().promotePlayersToSpies()

        val player1 = game.getDummy().apply {
            chasedByZombies(Zombies(game), `Zombies!!!`(game))
        }
        every { player1.decideToDrawNoCardsNextTurnForHide() } returns true

        val player2 = game.getNextPlayer(player1)

        // when

        player1.play(Hide(game))

        // then

        game.assertEvent(PlayedHide(player1, null, null, true))

        player1.getZombiesAroundCount() shouldBe 5 // Zombies, Zombies!!!
        player2.getZombiesAroundCount() shouldBe 0
    }
})
