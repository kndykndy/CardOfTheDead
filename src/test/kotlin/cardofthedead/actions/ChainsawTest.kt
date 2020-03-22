package cardofthedead.actions

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithDeck
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.cards.EmptyDeck
import cardofthedead.cards.Zombie
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

        val deck = EmptyDeck()

        val player = dummyPlayer().apply {
            chasedByZombies(
                deck.addCard(RedneckZombie()) as Zombie,
                deck.addCard(LadZombie()) as Zombie,
                deck.addCard(BrideZombie()) as Zombie
            )
        }

        val game = gameWithDeck(player, deck)

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

        val deck = EmptyDeck()

        val player = dummyPlayer().apply {
            chasedByZombies(
                deck.addCard(RedneckZombie()) as Zombie
            )
        }

        val game = gameWithDeck(player, deck)

        // when
        player.play(Chainsaw())

        // then

        game.discardDeck.size() shouldBe 1 // RedneckZombie
        player.getZombiesAroundCount() shouldBe 0
    }

    "should discard Zombies card when Zombies plus single zombie around" {
        // given

        val deck = EmptyDeck()

        val player = dummyPlayer().apply {
            chasedByZombies(
                deck.addCard(Zombies()) as Zombie,
                deck.addCard(RedneckZombie()) as Zombie
            )
        }

        val game = gameWithDeck(player, deck)

        // when
        player.play(Chainsaw())

        // then

        game.discardDeck.size() shouldBe 1 // Zombies
        player.getZombiesAroundCount() shouldBe 1 // RedneckZombie
    }

    "should do nothing when only Zombies!!! around" {
        // given

        val deck = EmptyDeck()

        val player = dummyPlayer().apply {
            chasedByZombies(
                deck.addCard(`Zombies!!!`()) as Zombie
            )
        }

        val game = gameWithDeck(player, deck)

        // when
        player.play(Chainsaw())

        // then

        game.discardDeck.size() shouldBe 0
        player.getZombiesAroundCount() shouldBe 3 // Zombies!!!
    }
})