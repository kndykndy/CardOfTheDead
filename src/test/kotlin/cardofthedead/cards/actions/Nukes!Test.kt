package cardofthedead.cards.actions

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getDummy
import cardofthedead.TestUtils.takeToHand
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@Suppress("ClassName")
class `Nukes!Test` : StringSpec({

    "should discard all cards from hands and all zombies" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = game.getDummy().apply {
            takeToHand(Slugger(game), Hide(game))
            chasedByZombies(LadZombie(game), BrideZombie(game), `Zombies!!!`(game))
        }
        val player2 = game.getNextPlayer(player1).apply {
            takeToHand(Chainsaw(game))
            chasedByZombies(`Zombies!!!`(game))
        }
        val player3 = game.getNextPlayer(player2)

        // when
        
        player1.play(`Nukes!`(game))

        // then

        game.discardDeck.size() shouldBe 7 // all cards from hands and all zombie cards

        setOf(player1, player2, player3).forEach { player ->
            player.hand.isEmpty() shouldBe true
            player.getZombiesAroundCount() shouldBe 0
        }
    }
})
