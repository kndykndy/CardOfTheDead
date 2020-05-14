package cardofthedead.cards.actions

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk

class SluggerTest : StringSpec({

    "should discard 1 Zombie" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = spyk(game.getDummy()).apply {
            chasedByZombies(LadZombie(game), BrideZombie(game), GrannyZombie(game))
        }
        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns true

        // when

        player1.play(Slugger(game))

        // then

        game.discardDeck.size() shouldBe 1

        player1.getZombiesAroundCount() shouldBe 2 // Any 2 Zombies
    }

    "should discard no Zombies if no zombies at hand" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = spyk(game.getDummy())
        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns true

        // when

        player1.play(Slugger(game))

        // then

        game.discardDeck.size() shouldBe 0

        player1.getZombiesAroundCount() shouldBe 0
    }

    "should pick random card from another player" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = spyk(game.getDummy())
        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns false

        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(Chainsaw(game), Dynamite(game))
        }

        every { player1.choosePlayerToTakeCardFromForSlugger() } returns player2

        // when

        player1.play(Slugger(game))

        // then

        player1.hand.size() shouldBe 1 // Any of Chainsaw/Dynamite
        player2.hand.size() shouldBe 1 // Any of Chainsaw/Dynamite
    }

    "should pick random card from another player if no card on hand" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = spyk(game.getDummy())
        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns false

        val player2 = game.getNextPlayer(player1)

        every { player1.choosePlayerToTakeCardFromForSlugger() } returns player2

        // when
        
        player1.play(Slugger(game))

        // then

        player1.hand.size() shouldBe 0
        player2.hand.size() shouldBe 0
    }
})
