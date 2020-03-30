package cardofthedead.players

import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.gameWithStandardDeck
import cardofthedead.TestUtils.testPlayer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PlayerTest : StringSpec({

    "should pick 2 cards to candidates" {
        // given

        val game = gameWithStandardDeck()

        val player = testPlayer(game)

        val topCard = game.playDeck.cards.last()

        // when
        player.pickCandidateCards(2)

        // then

        player.candidatesToHand.size() shouldBe 2
        player.candidatesToHand.cards.first() shouldBe topCard
    }

    "should not pick any cards to candidates if no cards" {
        // given
        val player = testPlayer(gameWithEmptyDeck())

        // when
        player.pickCandidateCards(1)

        // then
        player.candidatesToHand.isEmpty() shouldBe true
    }

    // todo do these

    // "should not pick negative or zero amount of cards to candidates" {}

    // "should draw top card if drawing this turn" {}
    // "should not draw top card if no cards" {}
    // "should not draw top card if not drawing this turn" {}

    // "should take card to hand" {}
    // "should not take card to hand if it is already there" {}

    // "should put card on bottom of deck" {}
    // "should put card on bottom if deck if deck is empty" {}

    // "should be chased by zombie" {}

    // "should add movement points" {}
    // "should not add movement points for Bitten" {}

    // "should add survival points" {}
    // "should not add negative survival points" {}

    // "should get zombies around" {}

    // "should discard card" {}
    // "should discard hand" {}
    // "should discard candidates to hand" {}
    // "should discard zombies" {}
    // "should discard escape cards" {}
    // "should discard all cards" {}

    // "should die" {}
})
