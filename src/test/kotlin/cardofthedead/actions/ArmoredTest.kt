package cardofthedead.actions

import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Bitten
import cardofthedead.dummyPlayer
import cardofthedead.gameWithEmptyDeck
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

class ArmoredTest : StringSpec({

    "should play Armored if Bitten is on hand" {
        // given

        val bitten = Bitten()

        val player = dummyPlayer()

        val game = gameWithEmptyDeck(player)

        player.takeToHand(bitten)

        val playDeck = game.playDeck
        val playerHand = player.hand

        val gameDeckSize = playDeck.size()
        val handSize = playerHand.size()

        // when
        player.play(Armored())

        // then

        playerHand.size() shouldBe handSize - 1
        playerHand.cards shouldNotContain bitten
        playDeck.size() shouldBe gameDeckSize + 1
        playDeck.cards[0] shouldBe bitten
    }

    "should play Armored if Bitten is not on hand" {
        // given

        val player = dummyPlayer()

        val game = gameWithEmptyDeck(player)

        val playDeck = game.playDeck
        val playerHand = player.hand

        val gameDeckSize = playDeck.size()
        val handSize = playerHand.size()

        // when
        player.play(Armored())

        // then

        playerHand.size() shouldBe handSize
        playerHand.pickCardOfClass(Bitten::class.java) shouldBe null
        playDeck.size() shouldBe gameDeckSize
    }
})
