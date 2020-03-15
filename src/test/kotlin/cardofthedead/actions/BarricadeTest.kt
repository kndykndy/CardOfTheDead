package cardofthedead.actions

import cardofthedead.TestUtils.addCards
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.gameWithStandardDeck
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Barricade
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.`Nukes!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class BarricadeTest : StringSpec({

    "should play Barricade" {
        // given

        val bitten = Bitten()
        val armored = Armored()
        val nukes = `Nukes!`()

        val player = dummyPlayer()

        val game = gameWithStandardDeck(player).apply { playDeck.addCards(bitten, armored, nukes) }

        val gameDeckSize = game.playDeck.size()
        val handSize = player.hand.size()

        // when
        player.play(Barricade().also { it.gameContext = game })

        // then

        player.hand.size() shouldBe handSize + 2
        listOf(armored, nukes).forEach { player.hand.pickCard(it) shouldNotBe null }
        player.hand.pickCard(bitten) shouldBe null

        game.playDeck.size() shouldBe gameDeckSize - 2
        game.playDeck.cards[0] shouldBe bitten
    }

    "should play Barricade if three cards left" {
        // given

        val bitten = Bitten()
        val armored = Armored()
        val nukes = `Nukes!`()

        val player = dummyPlayer()

        val game = gameWithEmptyDeck(player).apply { playDeck.addCards(bitten, armored, nukes) }

        // when
        player.play(Barricade().also { it.gameContext = game })

        // then

        player.hand.size() shouldBe 2
        listOf(armored, nukes).forEach { player.hand.pickCard(it) shouldNotBe null }
        player.hand.pickCard(bitten) shouldBe null

        game.playDeck.size() shouldBe 1
        game.playDeck.cards[0] shouldBe bitten
    }

    "should play Barricade if two cards left" {
        // given

        val armored = Armored()
        val nukes = `Nukes!`()

        val player = dummyPlayer()

        val game = gameWithEmptyDeck(player).apply { playDeck.addCards(armored, nukes) }

        // when
        player.play(Barricade().also { it.gameContext = game })

        // then

        player.hand.size() shouldBe 1
        player.hand.pickCard(nukes) shouldNotBe null

        game.playDeck.size() shouldBe 1
        game.playDeck.cards[0] shouldBe armored
    }

    "should play Barricade if one card left" {
        // given

        val nukes = `Nukes!`()

        val player = dummyPlayer()

        val game = gameWithEmptyDeck(player).apply { playDeck.addCard(nukes) }

        // when
        player.play(Barricade().also { it.gameContext = game })

        // then

        player.hand.size() shouldBe 0

        game.playDeck.size() shouldBe 1
        game.playDeck.cards[0] shouldBe nukes
    }
})
