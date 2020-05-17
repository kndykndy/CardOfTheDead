package cardofthedead.cards.events

import cardofthedead.TestUtils.assertEvent
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedRingtone
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RingtoneTests : StringSpec({

    "should take a single zombie from all players" {
        // given

        val game = gameWithEmptyDeck()

        val player1 = game.getFirstPlayer().apply {
            chasedByZombie(LadZombie(game))
        }
        val player2 = game.getNextPlayer(player1).apply {
            chasedByZombies(GrannyZombie(game), Zombies(game))
        }
        val player3 = game.getNextPlayer(player2).apply {
            chasedByZombies(`Zombies!!!`(game), BrideZombie(game))
        }

        // when

        player1.play(Ringtone(game))

        // then

        game.assertEvent(PlayedRingtone(player1, 1, 3))

        player1.getZombiesAroundCount() shouldBe 3 // LadZombie, BrideZombie, GrannyZombie
        player2.getZombiesAroundCount() shouldBe 2 // Zombies
        player3.getZombiesAroundCount() shouldBe 3 // Zombies!!!
    }
})
