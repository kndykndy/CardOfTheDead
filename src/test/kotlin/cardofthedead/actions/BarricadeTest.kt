package cardofthedead.actions

import cardofthedead.addCards
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Barricade
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.`Nukes!`
import cardofthedead.game.Game
import cardofthedead.players.Player
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class BarricadeTest : StringSpec({

    "should play Barricade" {
        // given

        val bitten = Bitten()
        val armored = Armored()
        val nukes = `Nukes!`()

        val player = Player.of("John Doe")
        val game = Game.Builder().withPlayer(player).build().also {
            it.playDeck.addCards(listOf(bitten, armored, nukes))
        }

        val barricade = Barricade().also { it.gameContext = game }

        val gameDeckSize = game.playDeck.size()
        val handSize = player.hand.size()

        // when
        player.play(barricade)

        // then

        player.hand.size() shouldBe handSize + 2
        listOf(armored, nukes).forEach { player.hand.pickCard(it) shouldNotBe null }
        player.hand.pickCard(bitten) shouldBe null

        game.playDeck.size() shouldBe gameDeckSize - 2
        game.playDeck.cards[0] shouldBe bitten
    }
})
