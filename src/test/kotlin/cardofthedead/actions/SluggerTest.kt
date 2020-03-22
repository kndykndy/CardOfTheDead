package cardofthedead.actions

import cardofthedead.TestUtils
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.EmptyDeck
import cardofthedead.cards.Zombie
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Slugger
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

        val deck = EmptyDeck()

        val player1 = spyk(TestUtils.dummyPlayer()).apply {
            chasedByZombies(
                deck.addCard(LadZombie()) as Zombie,
                deck.addCard(BrideZombie()) as Zombie,
                deck.addCard(GrannyZombie()) as Zombie
            )
        }
        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns true

        val game = TestUtils.gameWithDeck(player1, deck)

        // when
        player1.play(Slugger().apply { gameContext = game })

        // then

        player1.getZombiesAroundCount() shouldBe 2 // Any 2 Zombies

        game.discardDeck.size() shouldBe 1
    }

    "should discard no Zombies if no zombies at hand" {
        // given

        val deck = EmptyDeck()

        val player1 = spyk(TestUtils.dummyPlayer())
        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns true

        val game = TestUtils.gameWithDeck(player1, deck)

        // when
        player1.play(Slugger().apply { gameContext = game })

        // then

        player1.getZombiesAroundCount() shouldBe 0

        game.discardDeck.size() shouldBe 0
    }

    "should pick random card from another player" {
        // given

        val deck = EmptyDeck()

        val player1 = spyk(TestUtils.dummyPlayer())
        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns false

        val game = TestUtils.gameWithDeck(player1, deck)

        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(deck.addCard(Chainsaw()), deck.addCard(Dynamite()))
        }

        every { player1.choosePlayerToTakeCardFromForSlugger() } returns player2

        // when
        player1.play(Slugger().apply { gameContext = game })

        // then

        player1.hand.size() shouldBe 1 // Any of Chainsaw/Dynamite
        player2.hand.size() shouldBe 1 // Any of Chainsaw/Dynamite
    }

    "should pick random card from another player if no card on hand" {
        // given

        val deck = EmptyDeck()

        val player1 = spyk(TestUtils.dummyPlayer())
        every { player1.decideToDiscardZombieOrTakeCardForSlugger() } returns false

        val game = TestUtils.gameWithDeck(player1, deck)

        val player2 = game.getNextPlayer(player1)

        every { player1.choosePlayerToTakeCardFromForSlugger() } returns player2

        // when
        player1.play(Slugger().apply { gameContext = game })

        // then

        player1.hand.size() shouldBe 0
        player2.hand.size() shouldBe 0
    }
})
