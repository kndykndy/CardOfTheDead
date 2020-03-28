package cardofthedead.actions

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.cards.actions.Chainsaw
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

        val player = dummyPlayer().apply {
            chasedByZombies(RedneckZombie(), LadZombie(), BrideZombie())
        }

        val game = gameWithEmptyDeck(player)

        // when
        player.play(Chainsaw())

        // then

        game.discardDeck.size() shouldBe 2 // Any 2 Zombie
        player.getZombiesAroundCount() shouldBe 1 // Any Zombie
    }

    "should do nothing when no zombies around" {
        // given

        val player = dummyPlayer()

        val game = gameWithEmptyDeck(player)

        // when
        player.play(Chainsaw())

        // then

        game.discardDeck.size() shouldBe 0
        player.getZombiesAroundCount() shouldBe 0
    }

    "should discard 1 of 1 zombies" {
        // given

        val player = dummyPlayer().apply {
            chasedByZombies(RedneckZombie())
        }

        val game = gameWithEmptyDeck(player)

        // when
        player.play(Chainsaw())

        // then

        game.discardDeck.size() shouldBe 1 // RedneckZombie
        player.getZombiesAroundCount() shouldBe 0
    }

    "should discard Zombies card when Zombies plus single zombie around" {
        // given

        val player = dummyPlayer().apply {
            chasedByZombies(Zombies(), RedneckZombie())
        }

        val game = gameWithEmptyDeck(player)

        // when
        player.play(Chainsaw())

        // then

        game.discardDeck.size() shouldBe 1 // Zombies
        player.getZombiesAroundCount() shouldBe 1 // RedneckZombie
    }

    "should do nothing when only Zombies!!! around" {
        // given

        val player = dummyPlayer().apply {
            chasedByZombies(`Zombies!!!`())
        }

        val game = gameWithEmptyDeck(player)

        // when
        player.play(Chainsaw())

        // then

        game.discardDeck.size() shouldBe 0
        player.getZombiesAroundCount() shouldBe 3 // Zombies!!!
    }
})