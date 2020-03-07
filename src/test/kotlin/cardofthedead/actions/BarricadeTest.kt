package cardofthedead.actions

import cardofthedead.addCards
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Barricade
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.`Nukes!`
import cardofthedead.game.Game
import cardofthedead.players.Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BarricadeTest {

    @Test
    fun `should play Barricade`() {
        // given

        val bitten = Bitten()
        val armored = Armored()
        val nukes = `Nukes!`()

        val player = Player.of("John Doe")
        val game = Game.Builder().withPlayer(player).build().also {
            it.playDeck.addCards(listOf(bitten, armored, nukes))
        }

        val barricade = Barricade().also { it.gameContext = game }

        val gameDeckSize = game.playDeck.size()
        val handSize = player.hand.size()

        // when
        barricade.play(player)

        // then
        assertEquals(handSize + 2, player.hand.size())
        listOf(armored, nukes).forEach { assertNotNull(player.hand.pickCard(it)) }
        assertNull(player.hand.pickCard(bitten))
        assertEquals(gameDeckSize - 2, game.playDeck.size())
        assertEquals(bitten, game.playDeck.cards[0])
    }

    @Test
    fun `should `() {
        // given
        // when
        // then
    }
}