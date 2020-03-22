package cardofthedead.actions

import cardofthedead.TestUtils.addCards
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.`Nukes!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ArmoredTest : StringSpec({

    "should put Bitten at bottom of deck" {
        // given

        val bitten = Bitten()

        val player = dummyPlayer().apply {
            takeToHand(bitten)
        }

        val game = gameWithEmptyDeck(player).apply { playDeck.addCards(Armored(), `Nukes!`()) }

        // when
        player.play(Armored())

        // then

        player.hand.size() shouldBe 0
        player.hand.hasCard(bitten) shouldBe false
        game.playDeck.size() shouldBe 3 // Bitten, Armored, Nukes!
        game.playDeck.cards[0] shouldBe bitten
    }

    "should do nothing if no Bitten on hand" {
        // given

        val player = dummyPlayer().apply {
            takeToHand(Armored(), `Nukes!`())
        }

        val game = gameWithEmptyDeck(player)

        // when
        player.play(Armored())

        // then

        player.hand.size() shouldBe 2
        player.hand.hasCardOfClass(Bitten::class.java) shouldBe false
        game.playDeck.size() shouldBe 0
    }
})
