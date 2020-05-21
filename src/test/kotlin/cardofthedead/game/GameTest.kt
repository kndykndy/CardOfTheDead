package cardofthedead.game

import cardofthedead.TestUtils.getValue
import cardofthedead.TestUtils.wrapPlayersAsSpyKs
import cardofthedead.cards.Card
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Lure
import cardofthedead.cards.actions.Slugger
import cardofthedead.decks.Deck
import cardofthedead.game.EventsFacade.Game.Amid.DrewZombie
import cardofthedead.game.EventsFacade.Game.Around.StartedNewGame
import cardofthedead.players.PlayerDescriptor
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import kotlin.reflect.KClass

class GameTest : StringSpec({

    "should play game" {
        // given

        val deck = object : Deck<Card>() {
            override fun getCardTypeToCardAmountInDeck(): Map<KClass<out Card>, Int> =
                mapOf(
                    Chainsaw::class to 1,
                    Slugger::class to 1,
                    Hide::class to 1,
                    Lure::class to 1
                )
        }

        val game = Game.Builder(
            PlayerDescriptor("Luke"),
            PlayerDescriptor("Obi Van"),
            deck
        ).build().wrapPlayersAsSpyKs()

//        val player1 = game.getFirstPlayer().apply {
//            takeToHand(Slugger(game), Hide(game), Lure(game))
//        }
//        val player2 = game.getNextPlayer(player1).apply {
//            takeToHand(chainsaw, Dynamite(game))
//        }

        // when

        game.play()

        // then

        val observer = game.getEventQueueTestObserver()
        observer.assertNoErrors()
        observer.assertComplete()

        observer.getValue(StartedNewGame::class).also {
            it.cardsInDeck shouldBeExactly 4
            it.playersCnt shouldBeExactly 2
        }

        observer.getValue(DrewZombie::class).also {
        }

    }
})
