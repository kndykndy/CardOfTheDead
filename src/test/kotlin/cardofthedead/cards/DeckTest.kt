package cardofthedead.cards

import cardofthedead.addCards
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
import io.kotest.matchers.collections.shouldBeOneOf
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class DeckTest : StringSpec({

    "should add unique cards to deck" {
        // given

        val armored = Armored()
        val deck = Deck<Card>().apply {
            addCard(armored)
            addCard(Barricade())
        }

        // when
        deck.addCard(armored)

        // then
        deck.size() shouldBe 2
    }

    "should add cards on deck' bottom for empty deck" {
        // given

        val dynamite = Dynamite()

        val deck = Deck<Card>()

        // when
        deck.addCardOnBottom(dynamite)

        // then
        deck.pickTopCard() shouldBe dynamite
    }

    "should add cards on deck' bottom" {
        // given

        val bitten = Bitten()
        val chainsaw = Chainsaw()
        val dynamite = Dynamite()

        val deck = Deck<Card>().apply { addCards(chainsaw, dynamite) }

        // when
        deck.addCardOnBottom(bitten)

        // then

        deck.pickTopCard() shouldBe dynamite
        deck.pickTopCard() shouldBe chainsaw
        deck.pickTopCard() shouldBe bitten
    }

    "should merge two empty decks" {
        // given

        val deck1 = Deck<Card>()
        val deck2 = Deck<Card>()

        // when
        deck1.merge(deck2)

        // then

        deck1.isEmpty() shouldBe true
        deck2.isEmpty() shouldBe true
    }

    "should merge empty deck to non-empty deck" {
        // given

        val deck1 = Deck<Card>().apply { addCard(Hide()) }
        val deck2 = Deck<Card>()

        // when
        deck1.merge(deck2)

        // then

        deck1.size() shouldBe 1
        deck2.isEmpty() shouldBe true
    }

    "should merge non-empty deck to empty deck" {
        // given

        val deck1 = Deck<Card>()
        val deck2 = Deck<Card>().apply { addCard(Lure()) }

        // when
        deck1.merge(deck2)

        // then

        deck1.size() shouldBe 1
        deck2.isEmpty() shouldBe true
    }

    "should merge to non-empty decks" {
        // given

        val deck1 = Deck<Card>().apply { addCard(Hide()) }
        val deck2 = Deck<Card>().apply { addCard(Lure()) }

        // when
        deck1.merge(deck2)

        // then

        deck1.size() shouldBe 2
        deck2.isEmpty() shouldBe true
    }

    "should pick top card from non-empty deck" {
        // given

        val nukes = `Nukes!`()
        val pillage = Pillage()

        val deck = Deck<Card>().apply { addCards(nukes, pillage) }

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
        val deck = Deck<Card>()

        // when
        val pickedCard = deck.pickTopCard()

        // then
        pickedCard shouldBe null
    }

    "should pick card if it exists in deck" {
        // given

        val slugger = Slugger()

        val deck = Deck<Card>().apply { addCard(slugger) }

        // when
        val pickedCard = deck.pickCard(slugger)

        // then

        pickedCard shouldBe slugger
        deck.isEmpty() shouldBe true
    }

    "should not pick card if does not exist in deck" {
        // given

        val slugger = Slugger()
        val tripped = Tripped()

        val deck = Deck<Card>().apply { addCard(tripped) }

        // when
        val pickedCard = deck.pickCard(slugger)

        // then

        pickedCard shouldBe null
        deck.size() shouldBe 1
    }

    "should pick card of class if it exists in deck" {
        // given

        val armored = Armored()

        val deck = Deck<Card>().apply { addCards(armored, Barricade()) }

        // when
        val pickedCardOfClass = deck.pickCardOfClass(Action::class.java)

        // then

        pickedCardOfClass shouldBe armored
        deck.size() shouldBe 1
    }

    "should not pick card of class from empty deck" {
        // given
        val deck = Deck<Card>()

        // when
        val pickedCardOfClass = deck.pickCardOfClass(Action::class.java)

        // then
        pickedCardOfClass shouldBe null
    }

    "should not pick card of class if it does not exist in deck" {
        // given
        val deck = Deck<Card>().apply { addCards(Armored(), Barricade()) }

        // when
        val pickedCardOfClass = deck.pickCardOfClass(Zombie::class.java)

        // then

        pickedCardOfClass shouldBe null
        deck.size() shouldBe 2
    }

    "should pick random card from deck" {
        // given

        val hide = Hide()
        val lure = Lure()

        val deck = Deck<Card>().apply { addCards(hide, lure) }

        // when
        val pickedRandomCard = deck.pickRandomCard()

        // then

        pickedRandomCard shouldBeOneOf listOf(hide, lure)
        deck.size() shouldBe 1
    }

    "should not pick random card if deck is empty" {
        // given
        val deck = Deck<Card>()

        // when
        val pickedRandomCard = deck.pickRandomCard()

        // then
        pickedRandomCard shouldBe null
    }

    "should get list of action cards from deck keeping them in deck" {
        // given

        val nukes = `Nukes!`()
        val pillage = Pillage()

        val deck = Deck<Card>().apply { addCards(nukes, pillage, GrannyZombie()) }

        // when
        val actions = deck.getActions()

        // then

        actions shouldHaveSize 2
        actions shouldContainAll listOf(nukes, pillage)
        deck.size() shouldBe 3
    }

    "should get empty list of action cards if no actions in deck" {
        // given
        val deck = Deck<Card>().apply { addCards(Fog(), `Zombies!!!`()) }

        // when
        val actions = deck.getActions()

        // then

        actions.shouldBeEmpty()
        deck.size() shouldBe 2
    }

    "should get total movement points" {
        // given
        val deck = Deck<Action>().apply { addCards(Slugger(), Dynamite(), Pillage()) }

        // when
        val movementPoints = deck.getMovementPoints()

        // then

        movementPoints shouldBe 5
        deck.size() shouldBe 3
    }

    "should get 0 movement points if deck is empty" {
        // given
        val deck = Deck<Action>()

        // when
        val movementPoints = deck.getMovementPoints()

        // then

        movementPoints shouldBe 0
        deck.isEmpty() shouldBe true
    }

    "should get total zombie count" {
        // given
        val deck = Deck<Zombie>().apply { addCards(GrannyZombie(), Zombies(), `Zombies!!!`()) }

        // when
        val zombiesCount = deck.getZombiesCount()

        // then

        zombiesCount shouldBe 6
        deck.size() shouldBe 3
    }

    "should get 0 zombies points if zombie deck is empty" {
        // given
        val deck = Deck<Zombie>()

        // when
        val zombiesCount = deck.getZombiesCount()

        // then

        zombiesCount shouldBe 0
        deck.isEmpty() shouldBe true
    }

    "should get single zombies" {
        // given

        val granny = GrannyZombie()
        val lad = LadZombie()

        val deck = Deck<Zombie>().apply { addCards(granny, lad, Zombies(), `Zombies!!!`()) }

        // when
        val singleZombies = deck.getSingleZombies()

        // then

        singleZombies shouldHaveSize 2
        singleZombies shouldContainAll listOf(granny, lad)
        deck.size() shouldBe 4
    }

    "should get 0 single zombies if no such in deck" {
        // given
        val deck = Deck<Zombie>()

        // when
        val singleZombies = deck.getSingleZombies()

        // then

        singleZombies.shouldBeEmpty()
        deck.isEmpty() shouldBe true
    }
})
