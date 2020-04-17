package cardofthedead.players

import cardofthedead.TestUtils.chasedByZombies
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.TestUtils.gameWithStandardDeck
import cardofthedead.TestUtils.takeToCandidates
import cardofthedead.TestUtils.takeToHand
import cardofthedead.TestUtils.testPlayer
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Lure
import cardofthedead.cards.actions.Pillage
import cardofthedead.cards.actions.`Nukes!`
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.spyk

class PlayerTest : ShouldSpec({

    "when picking cards to candidates" {

        should("get requested amount if available") {
            // given

            val game = gameWithStandardDeck()
            val topCard = game.playDeck.cards.last()
            val playDeckSize = game.playDeck.size()

            val player = testPlayer(game)

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

            val player = spyk(testPlayer(game)).also {
                every { it.decideToDrawNoCardsNextTurnForHide() } returns true
            }

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

        should("do it") {
            // given

            val game = gameWithEmptyDeck()

            val player = testPlayer(game)

            val lure = Lure(game)

            // when
            player.takeToHand(lure)

            // then
            player.hand.hasCard(lure) shouldBe true
        }

        should("not do it if card is already in deck") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)

            val player = testPlayer(game).apply {
                takeToHand(lure)
            }

            // when
            player.takeToHand(lure)

            // then

            player.hand.size() shouldBe 1
            player.hand.hasCard(lure) shouldBe true
        }
    }

    "when putting card on bottom of deck" {

        should("do it") {
            // given

            val game = gameWithStandardDeck()
            val playDeckSize = game.playDeck.size()

            val player = testPlayer(game)

            val lure = Lure(game)

            // when
            player.putOnBottom(lure)

            // then

            game.playDeck.size() shouldBe playDeckSize + 1
            game.playDeck.cards.first() shouldBe lure
        }

        should("do it if deck is empty") {
            // given

            val game = gameWithEmptyDeck()

            val player = testPlayer(game)

            val lure = Lure(game)

            // when
            player.putOnBottom(lure)

            // then

            game.playDeck.size() shouldBe 1
            game.playDeck.cards.first() shouldBe lure
            game.playDeck.cards.last() shouldBe lure
        }
    }

    "when dealing with zombies" {

        should("be chased by it") {
            // given

            val game = gameWithEmptyDeck()

            val player = testPlayer(game)

            // when
            player.chasedByZombie(`Zombies!!!`(game))

            // then
            player.getZombiesAroundCount() shouldBe 3
        }

        should("be chased by it too") {
            // given

            val game = gameWithEmptyDeck()

            val player = testPlayer(game).apply {
                chasedByZombies(LadZombie(game), Zombies(game))
            }

            // when
            player.chasedByZombie(`Zombies!!!`(game))

            // then
            player.getZombiesAroundCount() shouldBe 6
        }

        should("add movement points") {
            // given

            val game = gameWithEmptyDeck()

            val player = testPlayer(game)

            // when
            player.addMovementPoints(Hide(game))

            // then
            player.getMovementPoints() shouldBe 1
        }

        should("add more movement points") {
            // given

            val game = gameWithEmptyDeck()

            val player = testPlayer(game).apply {
                addMovementPoints(Hide(game))
            }

            // when
            player.addMovementPoints(Dynamite(game))

            // then
            player.getMovementPoints() shouldBe 3
        }

        should("not add movement points for Bitten") {
            // given

            val game = gameWithEmptyDeck()

            val player = testPlayer(game)

            // when
            val exception = shouldThrow<IllegalArgumentException> {
                player.addMovementPoints(Bitten(game))
            }

            // then
            exception.message shouldBe "Cannot add movement points for Bitten"
            player.escapeCards.isEmpty() shouldBe true
        }

        should("add survival points") {
            // given
            val player = testPlayer(gameWithEmptyDeck())

            // when
            player.addSurvivalPoints(5)

            // then
            player.getSurvivalPoints() shouldBe 5
        }

        should("add more survival points") {
            // given
            val player = testPlayer(gameWithEmptyDeck()).apply {
                addSurvivalPoints(3)
            }

            // when
            player.addSurvivalPoints(5)

            // then
            player.getSurvivalPoints() shouldBe 8
        }

        should("not add negative survival points") {
            // given
            val player = testPlayer(gameWithEmptyDeck())

            // when-then

            shouldThrow<IllegalArgumentException> {
                player.addSurvivalPoints(-1)
            }
        }
    }

    "when dealing with discarding" {

        should("discard card") {
            // given

            val game = gameWithEmptyDeck()

            val player = testPlayer(game)

            val lure = Lure(game)

            // when
            player.discard(lure)

            // then
            game.discardDeck.hasCard(lure) shouldBe true
        }

        should("discard hand") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)
            val dynamite = Dynamite(game)
            val pillage = Pillage(game)

            val player = testPlayer(game).apply {
                takeToHand(lure, dynamite, pillage)
            }

            // when
            player.discardHand()

            // then

            player.hand.isEmpty() shouldBe true
            game.discardDeck.size() shouldBe 3 // all hand
            game.discardDeck.cards shouldContainAll listOf(lure, dynamite, pillage)
        }

        should("discard candidates to hand") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)
            val chainsaw = Chainsaw(game)

            val player = testPlayer(game).apply {
                takeToCandidates(lure, chainsaw)
            }

            // when
            player.discardCandidatesCards()

            // then

            player.hand.isEmpty() shouldBe true
            player.candidatesToHand.isEmpty() shouldBe true
            game.discardDeck.size() shouldBe 2 // all candidates
            game.discardDeck.cards shouldContainAll listOf(lure, chainsaw)
        }

        should("discard zombies") {
            // given

            val game = gameWithEmptyDeck()

            val zombie = LadZombie(game)
            val zombies = Zombies(game)

            val player = testPlayer(game).apply {
                chasedByZombies(zombie, zombies)
            }

            // when
            player.discardZombiesAround()

            // then

            player.getZombiesAroundCount() shouldBe 0
            game.discardDeck.size() shouldBe 2 // all zombies
            game.discardDeck.cards shouldContainAll listOf(zombie, zombies)
        }

        should("discard escape cards") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)
            val nukes = `Nukes!`(game)

            val player = testPlayer(game).apply {
                addMovementPoints(lure)
                addMovementPoints(nukes)
            }

            // when
            player.discardEscapeCards()

            // then

            player.hand.isEmpty() shouldBe true
            player.getMovementPoints() shouldBe 0
            game.discardDeck.size() shouldBe 2 // all escapes
            game.discardDeck.cards shouldContainAll listOf(lure, nukes)
        }

        should("discard all cards") {
            // given

            val game = gameWithEmptyDeck()

            val lure = Lure(game)
            val hide = Hide(game)
            val zombie = GrannyZombie(game)
            val nukes = `Nukes!`(game)

            val player = testPlayer(game).apply {
                takeToHand(lure)
                takeToCandidates(hide)
                chasedByZombie(zombie)
                addMovementPoints(nukes)
            }

            // when
            player.discardAllCards()

            // then

            player.hand.isEmpty() shouldBe true
            player.candidatesToHand.isEmpty() shouldBe true
            player.getZombiesAroundCount() shouldBe 0
            player.getMovementPoints() shouldBe 0
            game.discardDeck.size() shouldBe 4 // all cards
            game.discardDeck.cards shouldContainAll listOf(lure, hide, zombie, nukes)
        }
    }

    should("die") {
        // given

        val game = gameWithEmptyDeck()

        val player = testPlayer(game).apply {
            takeToHand(Lure(game))
            takeToCandidates(Hide(game))
            chasedByZombie(GrannyZombie(game))
            addMovementPoints(`Nukes!`(game))
            addSurvivalPoints(5)
        }

        // when
        player.die()

        // then

        player.hand.isEmpty() shouldBe true
        player.candidatesToHand.isEmpty() shouldBe true
        player.getZombiesAroundCount() shouldBe 0
        player.getMovementPoints() shouldBe 0
        game.discardDeck.size() shouldBe 4 // all cards
        player.getSurvivalPoints() shouldBe 5
    }
})
