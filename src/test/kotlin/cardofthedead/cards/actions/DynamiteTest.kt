package cardofthedead.cards.actions

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.TestUtils.wrapPlayersAsSpyKs
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.RedneckZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedDynamite
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every

class DynamiteTest : StringSpec({

    "should discard 3 zombies and 1 movement card" {
        // given

        val game = gameWithEmptyDeck().wrapPlayersAsSpyKs()

        val redneckZombie = RedneckZombie(game)
        val ladZombie = LadZombie(game)
        val brideZombie = BrideZombie(game)
        val armored = Armored(game)

        val player = game.getFirstPlayer().apply {
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

        game.discardDeck.cards shouldContainExactly
                listOf(redneckZombie, ladZombie, brideZombie, armored)
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

        game.discardDeck.cards shouldContainExactly listOf(armored)
        game.assertEvent(PlayedDynamite(player, emptyList(), armored))

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

        game.discardDeck.cards shouldContainExactly listOf(ladZombie)
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

        game.discardDeck.cards shouldContainExactly listOf(zombiesExcl, armored)
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

        game.discardDeck.cards shouldContainExactly listOf(zombies, ladZombie, armored)
        game.assertEvent(PlayedDynamite(player, listOf(zombies, ladZombie), armored))

        player.getZombiesAroundCount() shouldBe 0
        player.getMovementPoints() shouldBe 0
    }
})
