package cardofthedead.cards.actions

import cardofthedead.TestUtils.addCards
import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.TestUtils.takeToHand
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedArmored
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ArmoredTest : StringSpec({

    "should put Bitten at bottom of deck" {
        // given

        val game = gameWithEmptyDeck().apply {
            playDeck.addCards(Armored(this), `Nukes!`(this))
        }

        val bitten = Bitten(game)

        val player = game.getDummy().apply {
            takeToHand(bitten)
        }

        // when
        player.play(Armored(game))

        // then

        player.hand.size() shouldBe 0
        player.hand.hasCard(bitten) shouldBe false
        game.playDeck.size() shouldBe 3 // Bitten, Armored, Nukes!
        game.playDeck.cards.first() shouldBe bitten

        game.assertEvent(PlayedArmored(player, true))
    }

    "should do nothing if no Bitten on hand" {
        // given

        val game = gameWithEmptyDeck()

        val player = game.getDummy().apply {
            takeToHand(Armored(game), `Nukes!`(game))
        }

        // when
        player.play(Armored(game))

        // then

        player.hand.size() shouldBe 2 // Armored, Nukes!
        player.hand.hasCardOfClass(Bitten::class.java) shouldBe false
        game.playDeck.size() shouldBe 0

        game.assertEvent(PlayedArmored(player, false))
    }
})
