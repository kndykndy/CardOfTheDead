package cardofthedead.cards.actions

import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.RedneckZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedChainsaw
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class ChainsawTest : StringSpec({

    "should discard 2 of 3 single zombies" {
        // given

        val game = gameWithEmptyDeck()

        val ladZombie = LadZombie(game)
        val brideZombie = BrideZombie(game)
        val player = game.getFirstPlayer().apply {
            chasedByZombies(RedneckZombie(game), ladZombie, brideZombie)
        }

        // when

        player.play(Chainsaw(game))

        // then

        game.discardDeck.cards shouldContainExactly listOf(ladZombie, brideZombie)
        game.assertEvent(PlayedChainsaw(player, listOf(ladZombie, brideZombie)))

        player.getZombiesAroundCount() shouldBe 1 // Any Zombie
    }

    "should do nothing when no zombies around" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getFirstPlayer()

        // when

        player.play(Chainsaw(game))

        // then

        game.discardDeck.size() shouldBe 0
        game.assertEvent(PlayedChainsaw(player, emptyList()))

        player.getZombiesAroundCount() shouldBe 0
    }

    "should discard 1 of 1 zombies" {
        // given

        val game = gameWithEmptyDeck()

        val redneckZombie = RedneckZombie(game)
        val player = game.getFirstPlayer().apply {
            chasedByZombies(redneckZombie)
        }

        // when

        player.play(Chainsaw(game))

        // then

        game.discardDeck.cards shouldContainExactly listOf(redneckZombie)
        game.assertEvent(PlayedChainsaw(player, listOf(redneckZombie)))

        player.getZombiesAroundCount() shouldBe 0
    }

    "should discard Zombies card when Zombies plus single zombie around" {
        // given

        val game = gameWithEmptyDeck()

        val zombies = Zombies(game)
        val player = game.getFirstPlayer().apply {
            chasedByZombies(zombies, RedneckZombie(game))
        }

        // when

        player.play(Chainsaw(game))

        // then

        game.discardDeck.cards shouldContainExactly listOf(zombies)
        game.assertEvent(PlayedChainsaw(player, listOf(zombies)))

        player.getZombiesAroundCount() shouldBe 1 // RedneckZombie
    }

    "should do nothing when only Zombies!!! around" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getFirstPlayer().apply {
            chasedByZombies(`Zombies!!!`(game))
        }

        // when

        player.play(Chainsaw(game))

        // then

        game.discardDeck.size() shouldBe 0
        game.assertEvent(PlayedChainsaw(player, emptyList()))

        player.getZombiesAroundCount() shouldBe 3 // Zombies!!!
    }
})
