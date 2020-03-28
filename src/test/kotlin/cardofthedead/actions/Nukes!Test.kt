package cardofthedead.actions

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.dummyPlayer
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.takeToHand
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

    "should discard all cards from hands and all zombies" {
        // given

        val player1 = dummyPlayer().apply {
            takeToHand(Slugger(), Hide())
            chasedByZombies(LadZombie(), BrideZombie(), `Zombies!!!`())
        }

        val game = gameWithEmptyDeck(player1)

        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(Chainsaw())
            chasedByZombies(`Zombies!!!`())
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
