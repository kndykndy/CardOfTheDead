package cardofthedead.actions

import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Bitten
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

class ArmoredTest : StringSpec({

    "should put Bitten at bottom of deck" {
        // given

        val bitten = Bitten()

        val player = dummyPlayer()

        val game = gameWithEmptyDeck(player)

        player.takeToHand(bitten)

        val gameDeckSize = game.playDeck.size()
        val handSize = player.hand.size()

        // when
        player.play(Armored())

        // then

        player.hand.size() shouldBe handSize - 1
        player.hand.cards shouldNotContain bitten
        game.playDeck.size() shouldBe gameDeckSize + 1
        game.playDeck.cards[0] shouldBe bitten
    }

    "should do nothing if no Bitten on hand" {
        // given

        val player = dummyPlayer()

        val game = gameWithEmptyDeck(player)

        val gameDeckSize = game.playDeck.size()
        val handSize = player.hand.size()

        // when
        player.play(Armored())

        // then

        player.hand.size() shouldBe handSize
        player.hand.hasCardOfClass(Bitten::class.java) shouldBe false
        game.playDeck.size() shouldBe gameDeckSize
    }
})
