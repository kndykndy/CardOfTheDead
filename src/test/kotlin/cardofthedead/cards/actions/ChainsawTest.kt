package cardofthedead.cards.actions

import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.RedneckZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedChainsaw
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ChainsawTest : StringSpec({

    "should discard 2 of 3 single zombies" {
        // given

        val game = gameWithEmptyDeck()

        val ladZombie = LadZombie(game)
        val brideZombie = BrideZombie(game)
        val player = game.getDummy().apply {
            chasedByZombies(RedneckZombie(game), ladZombie, brideZombie)
        }

        // when
        player.play(Chainsaw(game))

        // then

        player.getZombiesAroundCount() shouldBe 1 // Any Zombie

        game.discardDeck.size() shouldBe 2 // Any 2 Zombie
        game.assertEvent(PlayedChainsaw(player, listOf(ladZombie, brideZombie)))
    }

    "should do nothing when no zombies around" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getDummy()

        // when
        player.play(Chainsaw(game))

        // then

        player.getZombiesAroundCount() shouldBe 0

        game.discardDeck.size() shouldBe 0
        game.assertEvent(PlayedChainsaw(player, listOf()))
    }

    "should discard 1 of 1 zombies" {
        // given

        val game = gameWithEmptyDeck()

        val redneckZombie = RedneckZombie(game)
        val player = game.getDummy().apply {
            chasedByZombies(redneckZombie)
        }

        // when
        player.play(Chainsaw(game))

        // then

        player.getZombiesAroundCount() shouldBe 0

        game.discardDeck.size() shouldBe 1 // RedneckZombie
        game.assertEvent(PlayedChainsaw(player, listOf(redneckZombie)))
    }

    "should discard Zombies card when Zombies plus single zombie around" {
        // given

        val game = gameWithEmptyDeck()

        val zombies = Zombies(game)
        val player = game.getDummy().apply {
            chasedByZombies(zombies, RedneckZombie(game))
        }

        // when
        player.play(Chainsaw(game))

        // then

        player.getZombiesAroundCount() shouldBe 1 // RedneckZombie

        game.discardDeck.size() shouldBe 1 // Zombies
        game.assertEvent(PlayedChainsaw(player, listOf(zombies)))
    }

    "should do nothing when only Zombies!!! around" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getDummy().apply {
            chasedByZombies(`Zombies!!!`(game))
        }

        // when
        player.play(Chainsaw(game))

        // then

        player.getZombiesAroundCount() shouldBe 3 // Zombies!!!

        game.discardDeck.size() shouldBe 0
        game.assertEvent(PlayedChainsaw(player, listOf()))
    }
})
