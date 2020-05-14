package cardofthedead.cards.actions

import cardofthedead.TestUtils.addCards
import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.gameWithStandardDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.cards.zombies.RedneckZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedBarricade
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class BarricadeTest : StringSpec({

    "should discard 1 card and take 2 cards" {
        // given

        val game = gameWithStandardDeck()

        val bitten = Bitten(game)
        val armored = Armored(game)
        val nukes = `Nukes!`(game)

        game.playDeck.addCards(bitten, armored, nukes)

        val player = game.getDummy()

        val gameDeckSize = game.playDeck.size()

        // when

        player.play(Barricade(game))

        // then

        game.playDeck.size() shouldBe gameDeckSize - 2 // Armored, Nukes!
        game.playDeck.cards.first() shouldBe bitten
        game.assertEvent(PlayedBarricade(player, 2))

        player.hand.size() shouldBe 2 // Armored, Nukes!
        listOf(armored, nukes).forEach { player.hand.hasCard(it) shouldBe true }
        player.hand.hasCard(bitten) shouldBe false
    }

    "should discard 1 card and take 2 cards even if those are Zombies" {
        // given

        val game = gameWithStandardDeck()

        val zombiesExcl = `Zombies!!!`(game)
        val zombies = Zombies(game)
        val redneckZombie = RedneckZombie(game)

        game.playDeck.addCards(zombiesExcl, zombies, redneckZombie)

        val player = game.getDummy()

        val gameDeckSize = game.playDeck.size()

        // when

        player.play(Barricade(game))

        // then

        game.playDeck.size() shouldBe gameDeckSize - 2 // Zombies, RedneckZombie
        game.playDeck.cards.first() shouldBe zombiesExcl
        game.assertEvent(PlayedBarricade(player, 0))

        player.hand.isEmpty() shouldBe true
        player.getZombiesAroundCount() shouldBe 3 // Zombies, RedneckZombie
    }

    "should discard 1 card and take 2 cards if 3 cards left in deck" {
        // given

        val game = gameWithEmptyDeck()

        val bitten = Bitten(game)
        val armored = Armored(game)
        val nukes = `Nukes!`(game)

        game.playDeck.addCards(bitten, armored, nukes)

        val player = game.getDummy()

        // when

        player.play(Barricade(game))

        // then

        game.playDeck.size() shouldBe 1 // Bitten
        game.playDeck.cards.first() shouldBe bitten
        game.assertEvent(PlayedBarricade(player, 2))

        player.hand.size() shouldBe 2 // Armored, Nukes!
        listOf(armored, nukes).forEach { player.hand.hasCard(it) shouldBe true }
        player.hand.hasCard(bitten) shouldBe false
    }

    "should discard 1 card and take no cards if 1 card left in deck" {
        // given

        val game = gameWithEmptyDeck()

        val nukes = `Nukes!`(game)

        game.playDeck.addCard(nukes)

        val player = game.getDummy()

        // when

        player.play(Barricade(game))

        // then

        game.playDeck.size() shouldBe 1 // Bitten
        game.playDeck.cards.first() shouldBe nukes
        game.assertEvent(PlayedBarricade(player, 0))

        player.hand.isEmpty() shouldBe true
    }
})
