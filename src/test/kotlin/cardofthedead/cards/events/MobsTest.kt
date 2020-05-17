package cardofthedead.cards.events

import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.gameWithStandardDeck
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Slugger
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedMobs
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MobsTest : StringSpec({

    "should pass Mobs to next player if have Slugger on hand" {
        // given

        val game = gameWithStandardDeck()

        val slugger = Slugger(game)
        val chainsaw = Chainsaw(game)
        val dynamite = Dynamite(game)

        val player1 = game.getFirstPlayer().apply {
            takeToHand(slugger)
        }
        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(chainsaw, dynamite)
        }

        // when

        player1.play(Mobs(game))

        // then

        game.playDeck.cards.first() shouldBe dynamite
        game.playDeck.cards[1] shouldBe chainsaw
        game.assertEvent(PlayedMobs(player1, mapOf(player1 to false, player2 to true)))

        player1.hand.size() shouldBe 1 // Slugger
        player1.hand.hasCard(slugger) shouldBe true
        player2.hand.isEmpty() shouldBe true
    }

    "should put hand on bottom of the deck if have no Slugger on hand" {
        // given

        val game = gameWithStandardDeck()

        val chainsaw = Chainsaw(game)
        val dynamite = Dynamite(game)

        val player1 = game.getFirstPlayer().apply {
            takeToHand(chainsaw, dynamite)
        }

        // when

        player1.play(Mobs(game))

        // then

        game.playDeck.cards.first() shouldBe dynamite
        game.playDeck.cards[1] shouldBe chainsaw
        game.assertEvent(PlayedMobs(player1, mapOf(player1 to true)))

        player1.hand.isEmpty() shouldBe true
    }
})
