package cardofthedead.actions

import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Bitten
import cardofthedead.game.Game
import cardofthedead.players.Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class ArmoredTest {

    @Test
    fun `should play Armored if Bitten is on hand`() {
        // given
        val armored = Armored()
        val bitten = Bitten()
        val player = Player.of("John Doe")
        val game = Game.Builder().withPlayer(player).build()
        player.takeToHand(bitten)

        val gameDeckSize = game.playDeck.size()
        val handSize = player.hand.size()

        // when
        armored.play(player)

        // then
        assertEquals(handSize - 1, player.hand.size())
        assertFalse(player.hand.cards.contains(bitten))
        assertEquals(gameDeckSize + 1, game.playDeck.size())
        assertEquals(bitten, game.playDeck.cards[0])
    }

    @Test
    fun `should play Armored if Bitten is not on hand`() {
        // given
        val armored = Armored()
        val player = Player.of("John Doe")
        val game = Game.Builder().withPlayer(player).build()

        val gameDeckSize = game.playDeck.size()
        val handSize = player.hand.size()

        // when
        armored.play(player)

        // then
        assertEquals(handSize, player.hand.size())
        assertNull(player.hand.pickCardOfClass(Bitten::class.java))
        assertEquals(gameDeckSize, game.playDeck.size())
    }

    @Test
    fun `should `() {
        // given
        // when
        // then
    }
}