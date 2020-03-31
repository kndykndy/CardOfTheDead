package cardofthedead.players

import cardofthedead.TestUtils.addCardsToCandidates
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.testEasyPlayer
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Lure
import cardofthedead.cards.zombies.GrannyZombie
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EasyPlayerTest : StringSpec({

    "should choose 2 single point cards" {
        // given

        val game = gameWithEmptyDeck()

        val lure = Lure(game)
        val hide = Hide(game)

        val player = testEasyPlayer(game).apply {
            addCardsToCandidates(lure, hide, Dynamite(game), GrannyZombie(game))
        }

        // when
        player.chooseSinglePointCards(2)

        // then

        player.candidatesToHand.size() shouldBe 2 // Dynamite, GrannyZombie
        player.candidatesToHand.hasCard(lure) shouldBe false
        player.candidatesToHand.hasCard(hide) shouldBe false

        player.hand.hasCard(lure) shouldBe true
        player.hand.hasCard(hide) shouldBe true
    }

    "should choose available single point cards if requested more" {
        // given

        val game = gameWithEmptyDeck()

        val lure = Lure(game)

        val player = testEasyPlayer(game).apply {
            addCardsToCandidates(lure)
        }

        // when
        player.chooseSinglePointCards(3)

        // then

        player.candidatesToHand.isEmpty() shouldBe true
        player.hand.hasCard(lure) shouldBe true
    }

    "should choose no single point cards if requested 0 or less" {
        // given

        val game = gameWithEmptyDeck()

        val lure = Lure(game)

        val player = testEasyPlayer(game).apply {
            addCardsToCandidates(lure)
        }

        // when
        player.chooseSinglePointCards(-1)

        // then

        player.candidatesToHand.hasCard(lure) shouldBe true
        player.hand.hasCard(lure) shouldBe false
    }

    // todo rest
})
