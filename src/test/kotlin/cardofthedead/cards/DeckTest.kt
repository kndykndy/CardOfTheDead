package cardofthedead.cards

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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DeckTest {

    @Test
    fun `should add unique cards to deck`() {
        // given
        val armored = Armored()
        val barricade = Barricade()
        val deck = Deck<Card>().apply {
            addCard(armored)
            addCard(barricade)
        }

        // when
        deck.addCard(armored)

        // then
        assertEquals(2, deck.size())
    }

    @Test
    fun `should add cards on deck' bottom for empty deck`() {
        // given
        val dynamite = Dynamite()
        val deck = Deck<Card>()

        // when
        deck.addCardOnBottom(dynamite)

        // then
        assertEquals(dynamite, deck.pickTopCard())
    }

    @Test
    fun `should add cards on deck' bottom`() {
        // given
        val bitten = Bitten()
        val chainsaw = Chainsaw()
        val dynamite = Dynamite()
        val deck = Deck<Card>().apply {
            addCard(chainsaw)
            addCard(dynamite)
        }

        // when
        deck.addCardOnBottom(bitten)

        // then
        assertEquals(dynamite, deck.pickTopCard())
        assertEquals(chainsaw, deck.pickTopCard())
        assertEquals(bitten, deck.pickTopCard())
    }

    @Test
    fun `should merge two empty decks`() {
        // given
        val deck1 = Deck<Card>()
        val deck2 = Deck<Card>()

        // when
        deck1.merge(deck2)

        // then
        assertTrue(deck1.isEmpty())
        assertTrue(deck2.isEmpty())
    }

    @Test
    fun `should merge empty deck to non-empty deck`() {
        // given
        val deck1 = Deck<Card>().apply { addCard(Hide()) }
        val deck2 = Deck<Card>()

        // when
        deck1.merge(deck2)

        // then
        assertEquals(1, deck1.size())
        assertTrue(deck2.isEmpty())
    }

    @Test
    fun `should merge non-empty deck to empty deck`() {
        // given
        val deck1 = Deck<Card>()
        val deck2 = Deck<Card>().apply { addCard(Lure()) }

        // when
        deck1.merge(deck2)

        // then
        assertEquals(1, deck1.size())
        assertTrue(deck2.isEmpty())
    }

    @Test
    fun `should merge to non-empty decks`() {
        // given
        val deck1 = Deck<Card>().apply { addCard(Hide()) }
        val deck2 = Deck<Card>().apply { addCard(Lure()) }

        // when
        deck1.merge(deck2)

        // then
        assertEquals(2, deck1.size())
        assertTrue(deck2.isEmpty())
    }

    @Test
    fun `should pick top card from non-empty deck`() {
        // given
        val nukes = `Nukes!`()
        val pillage = Pillage()
        val deck = Deck<Card>().apply {
            addCard(nukes)
            addCard(pillage)
        }

        // when
        val pickedCard1 = deck.pickTopCard()
        val pickedCard2 = deck.pickTopCard()

        // then
        assertEquals(pillage, pickedCard1)
        assertEquals(nukes, pickedCard2)
        assertTrue(deck.isEmpty())
    }

    @Test
    fun `should not pick card from empty deck`() {
        // given
        val deck = Deck<Card>()

        // when
        val pickedCard = deck.pickTopCard()

        // then
        assertNull(pickedCard)
    }

    @Test
    fun `should pick card if it exists in deck`() {
        // given
        val slugger = Slugger()
        val deck = Deck<Card>().apply { addCard(slugger) }

        // when
        val pickedCard = deck.pickCard(slugger)

        // then
        assertEquals(slugger, pickedCard)
        assertTrue(deck.isEmpty())
    }

    @Test
    fun `should not pick card if does not exist in deck`() {
        // given
        val slugger = Slugger()
        val tripped = Tripped()
        val deck = Deck<Card>().apply { addCard(tripped) }

        // when
        val pickedCard = deck.pickCard(slugger)

        // then
        assertNull(pickedCard)
        assertEquals(1, deck.size())
    }

    @Test
    fun `should pick card of class if it exists in deck`() {
        // given
        val armored = Armored()
        val deck = Deck<Card>().apply {
            addCard(armored)
            addCard(Barricade())
        }

        // when
        val pickedCardOfClass = deck.pickCardOfClass(Action::class.java)

        // then
        assertEquals(armored, pickedCardOfClass)
        assertEquals(1, deck.size())
    }

    @Test
    fun `should not pick card of class from empty deck`() {
        // given
        val deck = Deck<Card>()

        // when
        val pickedCardOfClass = deck.pickCardOfClass(Action::class.java)

        // then
        assertNull(pickedCardOfClass)
    }

    @Test
    fun `should not pick card of class if it does not exist in deck`() {
        // given
        val deck = Deck<Card>().apply {
            addCard(Armored())
            addCard(Barricade())
        }

        // when
        val pickedCardOfClass = deck.pickCardOfClass(Zombie::class.java)

        // then
        assertNull(pickedCardOfClass)
        assertEquals(2, deck.size())
    }

    @Test
    fun `should pick random card from deck`() {
        // given
        val hide = Hide()
        val lure = Lure()
        val deck = Deck<Card>().apply {
            addCard(hide)
            addCard(lure)
        }

        // when
        val pickedRandomCard = deck.pickRandomCard()

        // then
        assertTrue(pickedRandomCard == hide || pickedRandomCard == lure)
        assertEquals(1, deck.size())
    }

    @Test
    fun `should not pick random card if deck is empty`() {
        // given
        val deck = Deck<Card>()

        // when
        val pickedRandomCard = deck.pickRandomCard()

        // then
        assertNull(pickedRandomCard)
    }

    @Test
    fun `should get list of action cards from deck keeping them in deck`() {
        // given
        val nukes = `Nukes!`()
        val pillage = Pillage()
        val deck = Deck<Card>().apply {
            addCard(nukes)
            addCard(pillage)
            addCard(GrannyZombie())
        }

        // when
        val actions = deck.getActions()

        // then
        assertEquals(2, actions.size)
        assertTrue(actions.containsAll(listOf(nukes, pillage)))
        assertEquals(3, deck.size())
    }

    @Test
    fun `should get empty list of action cards if no actions in deck`() {
        // given
        val deck = Deck<Card>().apply {
            addCard(Fog())
            addCard(`Zombies!!!`())
        }

        // when
        val actions = deck.getActions()

        // then
        assertTrue(actions.isEmpty())
        assertEquals(2, deck.size())
    }

    @Test
    fun `should get total movement points`() {
        // given
        val deck = Deck<Action>().apply {
            addCard(Slugger())
            addCard(Dynamite())
            addCard(Pillage())
        }

        // when
        val movementPoints = deck.getMovementPoints()

        // then
        assertEquals(5, movementPoints)
        assertEquals(3, deck.size())
    }

    @Test
    fun `should get 0 movement points if deck is empty`() {
        // given
        val deck = Deck<Action>()

        // when
        val movementPoints = deck.getMovementPoints()

        // then
        assertEquals(0, movementPoints)
        assertTrue(deck.isEmpty())
    }

    @Test
    fun `should get total zombie count`() {
        // given
        val deck = Deck<Zombie>().apply {
            addCard(GrannyZombie())
            addCard(Zombies())
            addCard(`Zombies!!!`())
        }

        // when
        val zombiesCount = deck.getZombiesCount()

        // then
        assertEquals(6, zombiesCount)
        assertEquals(3, deck.size())
    }

    @Test
    fun `should get 0 zombies points if zombie deck is empty`() {
        // given
        val deck = Deck<Zombie>()

        // when
        val zombiesCount = deck.getZombiesCount()

        // then
        assertEquals(0, zombiesCount)
        assertTrue(deck.isEmpty())
    }

    @Test
    fun `should get single zombies`() {
        // given
        val granny = GrannyZombie()
        val lad = LadZombie()
        val deck = Deck<Zombie>().apply {
            addCard(granny)
            addCard(lad)
            addCard(Zombies())
            addCard(`Zombies!!!`())
        }

        // when
        val singleZombies = deck.getSingleZombies()

        // then
        assertEquals(2, singleZombies.size)
        assertTrue(singleZombies.containsAll(listOf(granny, lad)))
        assertEquals(4, deck.size())
    }

    @Test
    fun `should get 0 single zombies if no such in deck`() {
        // given
        val deck = Deck<Zombie>()

        // when
        val singleZombies = deck.getSingleZombies()

        // then
        assertTrue(singleZombies.isEmpty())
        assertTrue(deck.isEmpty())
    }

    @Test
    fun `should `() {
        // given
        // when
        // then
    }
}