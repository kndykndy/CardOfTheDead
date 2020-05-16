package cardofthedead.cards.actions

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.RedneckZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedDynamite
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk

class DynamiteTest : StringSpec({

    "should discard 3 zombies and 1 movement card" {
        // given

        val game = gameWithEmptyDeck()

        val redneckZombie = RedneckZombie(game)
        val ladZombie = LadZombie(game)
        val brideZombie = BrideZombie(game)
        val armored = Armored(game)

        val player = spyk(game.getFirstPlayer()).apply {
            chasedByZombies(
                RedneckZombie(game),
                redneckZombie,
                ladZombie,
                brideZombie
            )
            addMovementPoints(armored, Hide(game))
        }
        every { player.chooseWorstMovementCardForDynamite() } returns armored

        // when

        player.play(Dynamite(game))

        // then

        game.discardDeck.size() shouldBe 4 // 3 zombies, 1 Armored
        game.assertEvent(
            PlayedDynamite(player, listOf(redneckZombie, ladZombie, brideZombie), armored)
        )

        player.getZombiesAroundCount() shouldBe 1
        player.getMovementPoints() shouldBe 1
    }

    "should discard 1 movement card and no zombies as no zombies around"{
        // given

        val game = gameWithEmptyDeck()

        val armored = Armored(game)

        val player = game.getFirstPlayer().apply {
            addMovementPoints(armored)
        }

        // when

        player.play(Dynamite(game))

        // then

        game.discardDeck.size() shouldBe 1
        game.assertEvent(PlayedDynamite(player, listOf(), armored))

        player.getZombiesAroundCount() shouldBe 0
        player.getMovementPoints() shouldBe 0
    }

    "should discard 1 zombie and no movement cards as no movement cards on hand"{
        // given

        val game = gameWithEmptyDeck()

        val ladZombie = LadZombie(game)

        val player = game.getFirstPlayer().apply {
            chasedByZombies(ladZombie)
        }

        // when

        player.play(Dynamite(game))

        // then

        game.discardDeck.size() shouldBe 1 // 1 zombie
        game.assertEvent(PlayedDynamite(player, listOf(ladZombie), null))

        player.getZombiesAroundCount() shouldBe 0
        player.getMovementPoints() shouldBe 0
    }

    "should discard Zombies!!! and 1 movement card"{
        // given

        val game = gameWithEmptyDeck()

        val zombiesExcl = `Zombies!!!`(game)
        val armored = Armored(game)

        val player = game.getFirstPlayer().apply {
            chasedByZombies(zombiesExcl)
            addMovementPoints(armored)
        }

        // when

        player.play(Dynamite(game))

        // then

        game.discardDeck.size() shouldBe 2 // Zombies!!!, Armored
        game.assertEvent(PlayedDynamite(player, listOf(zombiesExcl), armored))

        player.getZombiesAroundCount() shouldBe 0
        player.getMovementPoints() shouldBe 0
    }

    "should discard Zombies and zombie and 1 movement card"{
        // given

        val game = gameWithEmptyDeck()

        val zombies = Zombies(game)
        val ladZombie = LadZombie(game)
        val armored = Armored(game)

        val player = game.getFirstPlayer().apply {
            chasedByZombies(zombies, ladZombie)
            addMovementPoints(armored)
        }

        // when

        player.play(Dynamite(game))

        // then

        game.discardDeck.size() shouldBe 3 // Zombies, LadZombie, Armored
        game.assertEvent(PlayedDynamite(player, listOf(zombies, ladZombie), armored))

        player.getZombiesAroundCount() shouldBe 0
        player.getMovementPoints() shouldBe 0
    }
})
