package cardofthedead.players

import cardofthedead.TestUtils.addMovementPoints
import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.takeToCandidates
import cardofthedead.TestUtils.takeToHand
import cardofthedead.TestUtils.testEasyPlayer
import cardofthedead.cards.WayToPlayCard
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Lure
import cardofthedead.cards.actions.Pillage
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeOneOf
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk

class EasyPlayerTest : ShouldSpec({

    "when choosing single point cards" {

        should("get required amount if enough cards") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)
            val hide = Hide(game)

            val player = testEasyPlayer(game).apply {
                takeToCandidates(lure, hide, Dynamite(game), GrannyZombie(game))
            }

            // when
            player.chooseSinglePointCardsFromCandidates(2)

            // then

            player.candidatesToHand.size() shouldBe 2 // Dynamite, GrannyZombie
            player.candidatesToHand.hasCard(lure) shouldBe false
            player.candidatesToHand.hasCard(hide) shouldBe false

            player.hand.hasCard(lure) shouldBe true
            player.hand.hasCard(hide) shouldBe true
        }

        should("get available only if requested more") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)

            val player = testEasyPlayer(game).apply {
                takeToCandidates(lure)
            }

            // when
            player.chooseSinglePointCardsFromCandidates(3)

            // then

            player.candidatesToHand.isEmpty() shouldBe true
            player.hand.hasCard(lure) shouldBe true
        }

        should("get nothing if requested 0 or less") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)

            val player = testEasyPlayer(game).apply {
                takeToCandidates(lure)
            }

            // when
            player.chooseSinglePointCardsFromCandidates(-1)

            // then

            player.candidatesToHand.hasCard(lure) shouldBe true
            player.hand.hasCard(lure) shouldBe false
        }
    }

    "when deciding to play card from hand" {

        should("realise cannot play card if empty hand") {
            // given

            val game = gameWithEmptyDeck()

            val player = testEasyPlayer(game)

            // when
            val decision = player.decideToPlayCardFromHand()

            // then

            decision.wayToPlayCard shouldBe WayToPlayCard.CANNOT_PLAY
            decision.card shouldBe null
        }

        should("realise cannot play card if no playable cards") {
            // given

            val game = gameWithEmptyDeck()

            val player = testEasyPlayer(game).apply {
                takeToHand(Bitten(game))
            }

            // when
            val decision = player.decideToPlayCardFromHand()

            // then

            decision.wayToPlayCard shouldBe WayToPlayCard.CANNOT_PLAY
            decision.card shouldBe null
        }

        should("choose not to play any cards in 50% dice") {
            // given

            val game = gameWithEmptyDeck()

            val player = spyk(testEasyPlayer(game)).apply {
                takeToHand(Lure(game))
            }

            every { player.throwCoin() } returns false

            // when
            val decision = player.decideToPlayCardFromHand()

            // then

            decision.wayToPlayCard shouldBe WayToPlayCard.DO_NOT_PLAY
            decision.card shouldBe null
        }

        should("play card as MP if not surrounded by zombies and 50% dice") {
            // given

            val game = spyk(gameWithEmptyDeck())

            val lure = Lure(game)

            val player = spyk(testEasyPlayer(game)).apply {
                takeToHand(lure)
            }

            every { game.getZombiesCountToBeSurrounded() } returns 3

            every { player.throwCoin() } returnsMany listOf(true, true)
            every { player.getZombiesAroundCount() } returns 0

            // when
            val decision = player.decideToPlayCardFromHand()

            // then

            decision.wayToPlayCard shouldBe WayToPlayCard.PLAY_AS_MOVEMENT_POINTS
            decision.card shouldBe lure
        }

        should("play card as MP if lot of cards at hand") {
            // given

            val game = spyk(gameWithEmptyDeck())

            val lure = Lure(game)
            val slugger = Slugger(game)
            val hide = Hide(game)
            val pillage = Pillage(game)

            val player = spyk(testEasyPlayer(game)).apply {
                takeToHand(lure, slugger, hide, pillage)
            }

            every { game.getZombiesCountToBeSurrounded() } returns 3

            every { player.throwCoin() } returnsMany listOf(true, false)
            every { player.getZombiesAroundCount() } returns 3

            // when
            val decision = player.decideToPlayCardFromHand()

            // then

            decision.wayToPlayCard shouldBe WayToPlayCard.PLAY_AS_MOVEMENT_POINTS
            decision.card shouldBeOneOf listOf(lure, slugger, hide, pillage)
        }

        should("play card as action if surrounded or few cards on hand") {
            // given

            val game = spyk(gameWithEmptyDeck())

            val dynamite = Dynamite(game)

            val player = spyk(testEasyPlayer(game)).apply {
                takeToHand(dynamite)
            }

            every { game.getZombiesCountToBeSurrounded() } returns 3

            every { player.throwCoin() } returns true
            every { player.getZombiesAroundCount() } returns 3

            // when
            val decision = player.decideToPlayCardFromHand()

            // then

            decision.wayToPlayCard shouldBe WayToPlayCard.PLAY_AS_ACTION
            decision.card shouldBe dynamite
        }
    }

    "when choosing worst candidate for Barricade" {

        should("not pick any card if Candidates empty") {
            // given
            val player = testEasyPlayer(gameWithEmptyDeck())

            // when
            val card = player.chooseWorstCandidateForBarricade()

            // then
            card shouldBe null
        }

        should("pick card if only one in Candidates") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)

            val player = testEasyPlayer(game).apply {
                takeToCandidates(lure)
            }

            // when
            val card = player.chooseWorstCandidateForBarricade()

            // then
            card shouldBe lure
        }

        should("pick card if several in Candidates") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)
            val hide = Hide(game)
            val pillage = Pillage(game)

            val player = testEasyPlayer(game).apply {
                takeToCandidates(lure, hide, pillage)
            }

            // when
            val card = player.chooseWorstCandidateForBarricade()

            // then
            card shouldBeOneOf listOf(lure, hide, pillage)
        }
    }

    "when choosing worst movement card for Dynamite" {

        should("not pick card if Escape empty") {
            // given
            val player = testEasyPlayer(gameWithEmptyDeck())

            // when
            val card = player.chooseWorstMovementCardForDynamite()

            // then
            card shouldBe null
        }

        should("pick card if only one in Escape") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)

            val player = testEasyPlayer(game).apply {
                addMovementPoints(lure)
            }

            // when
            val card = player.chooseWorstMovementCardForDynamite()

            // then
            card shouldBe lure
        }

        should("pick card if several in Escape") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)
            val hide = Hide(game)
            val pillage = Pillage(game)

            val player = testEasyPlayer(game).apply {
                addMovementPoints(lure, hide, pillage)
            }

            // when
            val card = player.chooseWorstMovementCardForDynamite()

            // then
            card shouldBeOneOf listOf(lure, hide, pillage)
        }
    }

    "when deciding to discard a Zombie or take card for Slugger" {

        should("discard if too many zombies around and there is single") {
            // given

            val game = spyk(gameWithEmptyDeck())

            val player = testEasyPlayer(game).apply {
                chasedByZombies(LadZombie(game), `Zombies!!!`(game), Zombies(game))
            }

            every { game.getZombiesCountToBeSurrounded() } returns 3

            // when
            val decision = player.decideToDiscardZombieOrTakeCardForSlugger()

            // then
            decision shouldBe true
        }

        should("take if little zombies around") {
            // given

            val game = spyk(gameWithEmptyDeck())

            val player = testEasyPlayer(game)

            every { game.getZombiesCountToBeSurrounded() } returns 3

            // when
            val decision = player.decideToDiscardZombieOrTakeCardForSlugger()

            // then
            decision shouldBe false
        }

        should("take if no single zombies around") {
            // given

            val game = spyk(gameWithEmptyDeck())

            val player = testEasyPlayer(game).apply {
                chasedByZombies(`Zombies!!!`(game), Zombies(game))
            }

            every { game.getZombiesCountToBeSurrounded() } returns 3

            // when
            val decision = player.decideToDiscardZombieOrTakeCardForSlugger()

            // then
            decision shouldBe false
        }
    }
})
