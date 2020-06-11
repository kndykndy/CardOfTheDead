package cardofthedead.decks

import cardofthedead.TestUtils.addCards
import cardofthedead.TestUtils.gameWithEmptyDeck
import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.Zombie
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Barricade
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Lure
import cardofthedead.cards.actions.Pillage
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.actions.Tripped
import cardofthedead.cards.actions.`Nukes!`
import cardofthedead.cards.events.Fog
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class DeckTest : StringSpec({

    "should add unique cards to deck" {
        // given

        val game = gameWithEmptyDeck()

        val armored = Armored(game)

        val deck = EmptyDeck<Card>().apply {
            addCard(armored)
            addCard(Barricade(game))
        }

        // when

        deck.addCard(armored)

        // then

        deck.size() shouldBe 2
    }

    "should add cards on deck' bottom for empty deck" {
        // given

        val game = gameWithEmptyDeck()

        val dynamite = Dynamite(game)

        val deck = EmptyDeck<Card>()

        // when

        deck.addCardOnBottom(dynamite)

        // then

        deck.pickTopCard() shouldBe dynamite
    }

    "should add cards on deck' bottom" {
        // given

        val game = gameWithEmptyDeck()

        val bitten = Bitten(game)
        val chainsaw = Chainsaw(game)
        val dynamite = Dynamite(game)

        val deck = EmptyDeck<Card>().apply { addCards(chainsaw, dynamite) }

        // when

        deck.addCardOnBottom(bitten)

        // then

        deck.pickTopCard() shouldBe dynamite
        deck.pickTopCard() shouldBe chainsaw
        deck.pickTopCard() shouldBe bitten
    }

    "should merge two empty decks" {
        // given

        val deck1 = EmptyDeck<Card>()
        val deck2 = EmptyDeck<Card>()

        // when

        deck1.merge(deck2)

        // then

        deck1.isEmpty() shouldBe true
        deck2.isEmpty() shouldBe true
    }

    "should merge empty deck to non-empty deck" {
        // given

        val game = gameWithEmptyDeck()

        val deck1 = EmptyDeck<Card>().apply { addCard(Hide(game)) }
        val deck2 = EmptyDeck<Card>()

        // when

        deck1.merge(deck2)

        // then

        deck1.size() shouldBe 1
        deck2.isEmpty() shouldBe true
    }

    "should merge non-empty deck to empty deck" {
        // given

        val game = gameWithEmptyDeck()

        val deck1 = EmptyDeck<Card>()
        val deck2 = EmptyDeck<Card>().apply { addCard(Lure(game)) }

        // when

        deck1.merge(deck2)

        // then

        deck1.size() shouldBe 1
        deck2.isEmpty() shouldBe true
    }

    "should merge to non-empty decks" {
        // given

        val game = gameWithEmptyDeck()

        val deck1 = EmptyDeck<Card>().apply { addCard(Hide(game)) }
        val deck2 = EmptyDeck<Card>().apply { addCard(Lure(game)) }

        // when

        deck1.merge(deck2)

        // then

        deck1.size() shouldBe 2
        deck2.isEmpty() shouldBe true
    }

    "should return true if has card in deck" {
        // given

        val game = gameWithEmptyDeck()

        val hide = Hide(game)
        val deck = EmptyDeck<Card>().apply { addCard(hide) }

        // when

        val hasCard = deck.hasCard(hide)

        // then

        hasCard shouldBe true
    }

    "should return false if does not have card in deck" {
        // given

        val game = gameWithEmptyDeck()

        val deck = EmptyDeck<Card>().apply { addCard(Hide(game)) }

        // when

        val hasCard = deck.hasCard(Slugger(game))

        // then

        hasCard shouldBe false
    }

    "should return true if has card of class in deck" {
        // given

        val game = gameWithEmptyDeck()

        val deck = EmptyDeck<Card>().apply { addCard(Hide(game)) }

        // when

        val hasCard = deck.hasCardOfClass(Hide::class.java)

        // then

        hasCard shouldBe true
    }

    "should return true if does not have card of class in deck" {
        // given

        val game = gameWithEmptyDeck()

        val deck = EmptyDeck<Card>().apply { addCard(Hide(game)) }

        // when

        val hasCard = deck.hasCardOfClass(Slugger::class.java)

        // then

        hasCard shouldBe false
    }

    "should pick top card from non-empty deck" {
        // given

        val game = gameWithEmptyDeck()

        val nukes = `Nukes!`(game)
        val pillage = Pillage(game)

        val deck = EmptyDeck<Card>().apply { addCards(nukes, pillage) }

        // when

        val pickedCard1 = deck.pickTopCard()
        val pickedCard2 = deck.pickTopCard()

        // then

        pickedCard1 shouldBe pillage
        pickedCard2 shouldBe nukes
        deck.isEmpty() shouldBe true
    }

    "should not pick card from empty deck" {
        // given

        val deck = EmptyDeck<Card>()

        // when

        val pickedCard = deck.pickTopCard()

        // then

        pickedCard shouldBe null
    }

    "should pick card if it exists in deck" {
        // given

        val game = gameWithEmptyDeck()

        val slugger = Slugger(game)

        val deck = EmptyDeck<Card>().apply { addCard(slugger) }

        // when

        val pickedCard = deck.pickCard(slugger)

        // then

        pickedCard shouldBe slugger
        deck.isEmpty() shouldBe true
    }

    "should not pick card if does not exist in deck" {
        // given

        val game = gameWithEmptyDeck()

        val slugger = Slugger(game)
        val tripped = Tripped(game)

        val deck = EmptyDeck<Card>().apply { addCard(tripped) }

        // when

        val pickedCard = deck.pickCard(slugger)

        // then

        pickedCard shouldBe null
        deck.size() shouldBe 1
    }

    "should pick card by index if it exists in deck" {
        // given

        val game = gameWithEmptyDeck()

        val slugger = Slugger(game)

        val deck = EmptyDeck<Card>().apply { addCard(slugger) }

        // when

        val pickedCard = deck.pickCard(0)

        // then

        pickedCard shouldBe slugger
        deck.isEmpty() shouldBe true
    }

    "should not pick card by index if index is outside of deck capacity" {
        // given

        val game = gameWithEmptyDeck()

        val tripped = Tripped(game)

        val deck = EmptyDeck<Card>().apply { addCard(tripped) }

        // when

        val pickedCard = deck.pickCard(5)

        // then

        pickedCard shouldBe null
        deck.size() shouldBe 1
    }

    "should pick card of class if it exists in deck" {
        // given

        val game = gameWithEmptyDeck()

        val armored = Armored(game)

        val deck = EmptyDeck<Card>().apply { addCards(armored, Barricade(game)) }

        // when

        val pickedCardOfClass = deck.pickCardOfClass(Action::class.java)

        // then

        pickedCardOfClass shouldBe armored
        deck.size() shouldBe 1
    }

    "should not pick card of class from empty deck" {
        // given

        val deck = EmptyDeck<Card>()

        // when

        val pickedCardOfClass = deck.pickCardOfClass(Action::class.java)

        // then

        pickedCardOfClass shouldBe null
    }

    "should not pick card of class if it does not exist in deck" {
        // given

        val game = gameWithEmptyDeck()

        val deck = EmptyDeck<Card>().apply { addCards(Armored(game), Barricade(game)) }

        // when

        val pickedCardOfClass = deck.pickCardOfClass(Zombie::class.java)

        // then

        pickedCardOfClass shouldBe null
        deck.size() shouldBe 2
    }

    "should get list of action cards from deck keeping them in deck" {
        // given

        val game = gameWithEmptyDeck()

        val nukes = `Nukes!`(game)
        val pillage = Pillage(game)

        val deck = EmptyDeck<Card>().apply { addCards(nukes, pillage, GrannyZombie(game)) }

        // when

        val actions = deck.getActions()

        // then

        actions shouldContainExactly listOf(nukes, pillage)
        deck.size() shouldBe 3
    }

    "should get empty list of action cards if no actions in deck" {
        // given

        val game = gameWithEmptyDeck()

        val deck = EmptyDeck<Card>().apply { addCards(Fog(game), `Zombies!!!`(game)) }

        // when

        val actions = deck.getActions()

        // then

        actions.shouldBeEmpty()
        deck.size() shouldBe 2
    }

    "should get list of single action cards from deck keeping them in deck" {
        // given

        val game = gameWithEmptyDeck()

        val lure = Lure(game)
        val armored = Armored(game)

        val deck = EmptyDeck<Card>().apply {
            addCards(armored, lure, `Nukes!`(game), LadZombie(game))
        }

        // when

        val actions = deck.getSinglePointActions()

        // then

        actions shouldContainExactly listOf(armored, lure)
        deck.size() shouldBe 4
    }

    "should get total movement points" {
        // given

        val game = gameWithEmptyDeck()

        val deck = EmptyDeck<Action>()
            .apply { addCards(Slugger(game), Dynamite(game), Pillage(game)) }

        // when

        val movementPoints = deck.getMovementPoints()

        // then

        movementPoints shouldBe 5
        deck.size() shouldBe 3
    }

    "should get 0 movement points if deck is empty" {
        // given

        val deck = EmptyDeck<Action>()

        // when

        val movementPoints = deck.getMovementPoints()

        // then

        movementPoints shouldBe 0
        deck.isEmpty() shouldBe true
    }

    "should get single zombies" {
        // given

        val game = gameWithEmptyDeck()

        val granny = GrannyZombie(game)
        val lad = LadZombie(game)

        val deck = EmptyDeck<Zombie>()
            .apply { addCards(granny, lad, Zombies(game), `Zombies!!!`(game)) }

        // when

        val singleZombies = deck.getSingleZombies()

        // then

        singleZombies shouldContainExactly listOf(granny, lad)
        deck.size() shouldBe 4
    }

    "should get 0 single zombies if no such in deck" {
        // given

        val deck = EmptyDeck<Zombie>()

        // when

        val singleZombies = deck.getSingleZombies()

        // then

        singleZombies.shouldBeEmpty()
        deck.isEmpty() shouldBe true
    }
})
