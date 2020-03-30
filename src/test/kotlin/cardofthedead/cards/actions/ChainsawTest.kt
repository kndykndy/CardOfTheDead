package cardofthedead.cards.actions

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.RedneckZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ChainsawTest : StringSpec({

    "should discard 2 of 3 single zombies" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getDummy().apply {
            chasedByZombies(RedneckZombie(game), LadZombie(game), BrideZombie(game))
        }

        // when
        player.play(Chainsaw(game))

        // then

        game.discardDeck.size() shouldBe 2 // Any 2 Zombie
        player.getZombiesAroundCount() shouldBe 1 // Any Zombie
    }

    "should do nothing when no zombies around" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getDummy()

        // when
        player.play(Chainsaw(game))

        // then

        game.discardDeck.size() shouldBe 0
        player.getZombiesAroundCount() shouldBe 0
    }

    "should discard 1 of 1 zombies" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getDummy().apply {
            chasedByZombies(RedneckZombie(game))
        }

        // when
        player.play(Chainsaw(game))

        // then

        game.discardDeck.size() shouldBe 1 // RedneckZombie
        player.getZombiesAroundCount() shouldBe 0
    }

    "should discard Zombies card when Zombies plus single zombie around" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getDummy().apply {
            chasedByZombies(Zombies(game), RedneckZombie(game))
        }

        // when
        player.play(Chainsaw(game))

        // then

        game.discardDeck.size() shouldBe 1 // Zombies
        player.getZombiesAroundCount() shouldBe 1 // RedneckZombie
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

        game.discardDeck.size() shouldBe 0
        player.getZombiesAroundCount() shouldBe 3 // Zombies!!!
    }
})
