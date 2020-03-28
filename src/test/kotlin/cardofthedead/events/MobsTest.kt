package cardofthedead.events

import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithStandardDeck
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.events.Mobs
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MobsTest : StringSpec({

    "should pass Mobs to next player if have Slugger on hand" {
        // given

        val slugger = Slugger()
        val player1 = dummyPlayer().apply {
            takeToHand(slugger)
        }

        val game = gameWithStandardDeck(player1)

        val chainsaw = Chainsaw()
        val dynamite = Dynamite()
        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(chainsaw, dynamite)
        }

        // when
        player1.play(Mobs().apply { gameContext = game })

        // then

        player1.hand.size() shouldBe 1 // Slugger
        player1.hand.hasCard(slugger) shouldBe true

        player2.hand.isEmpty() shouldBe true
        game.playDeck.cards.first() shouldBe dynamite
        game.playDeck.cards[1] shouldBe chainsaw
    }

    "should put hand on bottom of the deck if have no Slugger on hand" {
        // given

        val chainsaw = Chainsaw()
        val dynamite = Dynamite()
        val player1 = dummyPlayer().apply {
            takeToHand(chainsaw, dynamite)
        }

        val game = gameWithStandardDeck(player1)

        // when
        player1.play(Mobs().apply { gameContext = game })

        // then

        player1.hand.isEmpty() shouldBe true
        game.playDeck.cards.first() shouldBe dynamite
        game.playDeck.cards[1] shouldBe chainsaw
    }
})
