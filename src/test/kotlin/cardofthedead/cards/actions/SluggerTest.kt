package cardofthedead.cards.actions

import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.TestUtils.takeToHand
import cardofthedead.TestUtils.wrapPlayersAsSpyKs
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedSlugger
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every

class SluggerTest : StringSpec({

    "should discard 1 Zombie" {
        // given

        val game = gameWithEmptyDeck().wrapPlayersAsSpyKs()

        val ladZombie = LadZombie(game)

        val player1 = game.getFirstPlayer().apply {
            chasedByZombies(ladZombie, BrideZombie(game), GrannyZombie(game))
        }

        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns true
        every { player1.throwDice(listOfCards = any()) } returns 0 // pick LadZombie

        // when

        player1.play(Slugger(game))

        // then

        game.discardDeck.cards shouldContainExactly listOf(ladZombie)
        game.assertEvent(PlayedSlugger(player1, ladZombie, null, null))

        player1.getZombiesAroundCount() shouldBe 2 // Any 2 Zombies
    }

    "should discard no Zombies if no zombies at hand" {
        // given

        val game = gameWithEmptyDeck().wrapPlayersAsSpyKs()

        val player1 = game.getFirstPlayer()

        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns true

        // when

        player1.play(Slugger(game))

        // then

        game.discardDeck.size() shouldBe 0
        game.assertEvent(PlayedSlugger(player1, null, null, null))

        player1.getZombiesAroundCount() shouldBe 0
    }

    "should pick random card from another player" {
        // given

        val game = gameWithEmptyDeck().wrapPlayersAsSpyKs()

        val chainsaw = Chainsaw(game)
        val dynamite = Dynamite(game)

        val player1 = game.getFirstPlayer()
        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(chainsaw, dynamite)
        }

        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns false
        every { player1.choosePlayerToTakeCardFromForSlugger() } returns player2
        every { player1.throwDice(deck = any()) } returns 1 // pick Dynamite

        // when

        player1.play(Slugger(game))

        // then

        game.assertEvent(PlayedSlugger(player1, null, dynamite, player2))

        player1.hand.cards shouldContainExactly listOf(dynamite)
        player2.hand.cards shouldContainExactly listOf(chainsaw)
    }

    "should not pick any cards from another player if no card on their hand" {
        // given

        val game = gameWithEmptyDeck().wrapPlayersAsSpyKs()

        val player1 = game.getFirstPlayer()
        val player2 = game.getNextPlayer(player1)

        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns false
        every { player1.choosePlayerToTakeCardFromForSlugger() } returns player2

        // when

        player1.play(Slugger(game))

        // then

        game.assertEvent(PlayedSlugger(player1, null, null, player2))

        player1.hand.size() shouldBe 0
        player2.hand.size() shouldBe 0
    }
})
