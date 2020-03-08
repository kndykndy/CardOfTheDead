package cardofthedead.actions

import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Bitten
import cardofthedead.game.Game
import cardofthedead.players.Player
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

class ArmoredTest : StringSpec({

    "should play Armored if Bitten is on hand" {
        // given
        val armored = Armored()

        val bitten = Bitten()
        val player = Player.of("John Doe")
        val game = Game.Builder().withPlayer(player).build()
        player.takeToHand(bitten)

        val gameDeckSize = game.playDeck.size()
        val handSize = player.hand.size()

        // when
        player.play(armored)

        // then
        player.hand.size() shouldBe handSize - 1
        player.hand.cards shouldNotContain bitten
        game.playDeck.size() shouldBe gameDeckSize + 1
        game.playDeck.cards[0] shouldBe bitten
    }

    "should play Armored if Bitten is not on hand" {
        // given
        val armored = Armored()

        val player = Player.of("John Doe")
        val game = Game.Builder().withPlayer(player).build()

        val gameDeckSize = game.playDeck.size()
        val handSize = player.hand.size()

        // when
        player.play(armored)

        // then
        player.hand.size() shouldBe handSize
        player.hand.pickCardOfClass(Bitten::class.java) shouldBe null
        game.playDeck.size() shouldBe gameDeckSize
    }
})
