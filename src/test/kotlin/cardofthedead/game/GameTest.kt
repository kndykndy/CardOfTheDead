package cardofthedead.game

import cardofthedead.TestUtils
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.TestUtils.takeToHand
import cardofthedead.TestUtils.wrapPlayersAsSpyKs
import cardofthedead.cards.Card
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Lure
import cardofthedead.cards.actions.Slugger
import cardofthedead.decks.Deck
import io.kotest.core.spec.style.StringSpec

class GameTest : StringSpec({

    "should play game" {
        // given

//        val deck = Deck<Card>
        val game = TestUtils.gameWithEmptyDeck().wrapPlayersAsSpyKs()

        val chainsaw = Chainsaw(game)
        val slugger = Slugger(game)
        val hide = Hide(game)
        val lure = Lure(game)

//        val player1 = game.getFirstPlayer().apply {
//            takeToHand(Slugger(game), Hide(game), Lure(game))
//        }
//        val player2 = game.getNextPlayer(player1).apply {
//            takeToHand(chainsaw, Dynamite(game))
//        }

        // when

        game.play()

        // then


    }
})
