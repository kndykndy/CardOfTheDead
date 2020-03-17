package cardofthedead.actions

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.RedneckZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DynamiteTest : StringSpec({

    "should discard 3 zombies and 1 movement card" {
        // given

        val player = dummyPlayer().apply {
            chasedByZombies(RedneckZombie(), RedneckZombie(), LadZombie(), BrideZombie())
            addMovementPoints(Armored(), Hide())
        }

        val game = gameWithEmptyDeck(player)

        val gameDiscardDeckSize = game.discardDeck.size()

        // when
        player.play(Dynamite().apply { gameContext = game })

        // then

        game.discardDeck.size() shouldBe gameDiscardDeckSize + 4 // 3 zombies, 1 Armored
        player.getZombiesAroundCount() shouldBe 1

        player.getMovementPointsCount() shouldBe 1
    }

    "should discard 1 movement card and no zombies as no zombies around"{
        // given

        val player = dummyPlayer().apply {
            addMovementPoints(Armored())
        }

        val game = gameWithEmptyDeck(player)

        val gameDiscardDeckSize = game.discardDeck.size()

        // when
        player.play(Dynamite().apply { gameContext = game })

        // then

        game.discardDeck.size() shouldBe gameDiscardDeckSize + 1
        player.getZombiesAroundCount() shouldBe 0

        player.getMovementPointsCount() shouldBe 0
    }

    "should discard 1 zombie and no movement cards as no movement cards on hand"{
        // given

        val player = dummyPlayer().apply {
            chasedByZombies(LadZombie())
        }

        val game = gameWithEmptyDeck(player)

        val gameDiscardDeckSize = game.discardDeck.size()

        // when
        player.play(Dynamite().apply { gameContext = game })

        // then

        game.discardDeck.size() shouldBe gameDiscardDeckSize + 1 // 1 zombie
        player.getZombiesAroundCount() shouldBe 0

        player.getMovementPointsCount() shouldBe 0
    }

    "should discard Zombies!!! and 1 movement card"{
        // given

        val player = dummyPlayer().apply {
            chasedByZombies(`Zombies!!!`())
            addMovementPoints(Armored())
        }

        val game = gameWithEmptyDeck(player)

        val gameDiscardDeckSize = game.discardDeck.size()

        // when
        player.play(Dynamite().apply { gameContext = game })

        // then

        game.discardDeck.size() shouldBe gameDiscardDeckSize + 2 // Zombies!!!, Armored
        player.getZombiesAroundCount() shouldBe 0

        player.getMovementPointsCount() shouldBe 0
    }

    "should discard Zombies and zombie and 1 movement card"{
        // given

        val player = dummyPlayer().apply {
            chasedByZombies(Zombies(), LadZombie())
            addMovementPoints(Armored())
        }

        val game = gameWithEmptyDeck(player)

        val gameDiscardDeckSize = game.discardDeck.size()

        // when
        player.play(Dynamite().apply { gameContext = game })

        // then

        game.discardDeck.size() shouldBe gameDiscardDeckSize + 3 // Zombies, LadZombie, Armored
        player.getZombiesAroundCount() shouldBe 0

        player.getMovementPointsCount() shouldBe 0
    }
})