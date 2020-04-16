package cardofthedead.players

import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.gameWithStandardDeck
import cardofthedead.TestUtils.testPlayer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.spyk

class PlayerTest : ShouldSpec({

    "when picking cards to candidates" {

        should("get requested amount if available") {
            // given

            val game = gameWithStandardDeck()

            val player = testPlayer(game)

            val topCard = game.playDeck.cards.last()
            val playDeckSize = game.playDeck.size()

            // when
            player.pickCandidateCards(2)

            // then

            game.playDeck.size() shouldBe playDeckSize - 2
            player.candidatesToHand.cards.forEach {
                game.playDeck.hasCard(it) shouldBe false
            }
            player.candidatesToHand.size() shouldBe 2
            player.candidatesToHand.cards.first() shouldBe topCard
        }

        should("get no cards if no cards available") {
            // given
            val player = testPlayer(gameWithEmptyDeck())

            // when
            player.pickCandidateCards(1)

            // then
            player.candidatesToHand.isEmpty() shouldBe true
        }

        should("not attempt to pick less than zero cards") {
            // given
            val player = testPlayer(gameWithEmptyDeck())

            // when-then

            shouldThrow<IllegalArgumentException> {
                player.pickCandidateCards(-1)
            }
        }
    }

    "when drawing top card" {

        should("draw only if drawing this turn") {
            // given

            val game = gameWithStandardDeck()

            val player = spyk(testPlayer(game))

            every { player.decideToDrawNoCardsNextTurnForHide() } returns true

            // when
            val topCard = player.drawTopCard()

            // then

            topCard shouldNotBe null
            game.playDeck.hasCard(topCard!!) shouldBe false
        }

        should("not get any cards if no cards available") {
            // given

            val player = spyk(testPlayer(gameWithEmptyDeck()))

            every { player.decideToDrawNoCardsNextTurnForHide() } returns false

            // when
            val topCard = player.drawTopCard()

            // then
            topCard shouldBe null
        }
    }

    "when adding card to hand" {
        should("do it if cards available") {
            // todo
        }

        should("not do it if card is already in deck") {
            // todo
        }
    }

    "when putting card on bottom of deck" {

        should("do it") {
            // todo
        }

        should("do it if deck is empty") {
            // todo
        }
    }

    "when " {

    }

    "when dealing with zombies" {

        should("be chased by it") {
            // + should get zombies around
            // todo
        }

        should("add movement points") {
            // + get mp
            // todo
        }

        should("not add movement points for Bitten") {
            // todo
        }

        should("add survival points") {
            // + get SP
            // todo
        }

        should("not add negative survival points") {
            // todo
        }
    }

    "when dealing with discarding" {

        should("discard card") {
            // todo
        }

        should("discard hand") {
            // todo
        }

        should("discard candidates to hand") {
            // todo
        }

        should("discard zombies") {
            // todo
        }

        should("discard escape cards") {
            // todo
        }

        should("discard all cards") {
            // todo
        }
    }

    should("die") {
        // todo
    }
})
