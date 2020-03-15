package cardofthedead.actions

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithDeck
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.EmptyDeck
import cardofthedead.cards.Zombie
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.actions.`Nukes!`
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@Suppress("ClassName")
class `Nukes!Test` : StringSpec({

    "should play Nukes!" {
        // given

        val deck = EmptyDeck()

        val player1 = dummyPlayer().apply {
            takeToHand(deck.addCard(Slugger()), deck.addCard(Hide()))
            chasedByZombies(
                deck.addCard(LadZombie()) as Zombie,
                deck.addCard(BrideZombie()) as Zombie,
                deck.addCard(`Zombies!!!`()) as Zombie
            )
        }

        val game = gameWithDeck(player1, deck)

        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(deck.addCard(Chainsaw()))
            chasedByZombies(
                deck.addCard(`Zombies!!!`()) as Zombie
            )
        }
        val player3 = game.getNextPlayer(player2)

        // when
        player1.play(`Nukes!`().apply { gameContext = game })

        // then
        setOf(player1, player2, player3).forEach { player ->
            player.hand.isEmpty() shouldBe true
            player.getZombiesAroundCount() shouldBe 0
        }
        game.discardDeck.size() shouldBe 7 // all cards from hands and all zombie cards
    }
})
