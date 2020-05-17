package cardofthedead.cards.events

import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.TestUtils.takeToHand
import cardofthedead.TestUtils.wrapPlayersAsSpyKs
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedFog
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every

class FogTest : StringSpec({

    "should play Fog" {
        // given

        val game = gameWithEmptyDeck().wrapPlayersAsSpyKs()

        val player1 = game.getFirstPlayer().apply {
            takeToHand(Slugger(game), Hide(game))
            chasedByZombies(LadZombie(game), BrideZombie(game), Zombies(game))
        }
        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(Chainsaw(game))
            chasedByZombies(`Zombies!!!`(game))
        }
        val player3 = game.getNextPlayer(player2)

        every { player1.throwDice(deck = any()) } returns 0
        every { player2.throwDice(deck = any()) } returns 0
        every { player3.throwDice(deck = any()) } returns 0

        // when

        player1.play(Fog(game))

        // then

        game.players.sumBy { it.getZombiesAroundCount() } shouldBe 7 // all zombies in the game
        game.players.sumBy { it.hand.size() } shouldBe 3 // Slugger, Hide, Chainsaw
        game.assertEvent(PlayedFog(player1, mapOf(player1 to 4, player2 to 0, player3 to 3)))
    }
})
