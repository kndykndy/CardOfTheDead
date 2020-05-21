package cardofthedead.game

import cardofthedead.TestUtils.getAnyOfValues
import cardofthedead.TestUtils.getFirstPlayer
import cardofthedead.TestUtils.getValue
import cardofthedead.TestUtils.getValues
import cardofthedead.TestUtils.wrapPlayersAsSpyKs
import cardofthedead.cards.Card
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Slugger
import cardofthedead.decks.Deck
import cardofthedead.game.EventsFacade.Game.Amid.AppointedFirstPlayer
import cardofthedead.game.EventsFacade.Game.Amid.AppointedNextPlayer
import cardofthedead.game.EventsFacade.Game.Amid.StartedNewRound
import cardofthedead.game.EventsFacade.Game.Amid.WonRoundCauseDeckOver
import cardofthedead.game.EventsFacade.Game.Amid.WonRoundCauseEscaped
import cardofthedead.game.EventsFacade.Game.Amid.WonRoundCauseOneAlive
import cardofthedead.game.EventsFacade.Game.Around.AnnouncedGameWinners
import cardofthedead.game.EventsFacade.Game.Around.StartedNewGame
import cardofthedead.players.PlayerDescriptor
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldNot
import kotlin.reflect.KClass

class GameTest : StringSpec({

    "should play game" {
        // given

        val game = Game.Builder(
            PlayerDescriptor("Luke"),
            PlayerDescriptor("Obi Van"),
            object : Deck<Card>() {
                override fun getCardTypeToCardAmountInDeck(): Map<KClass<out Card>, Int> =
                    mapOf(
                        Chainsaw::class to 10,
                        Slugger::class to 10
                    )
            }
        ).build().wrapPlayersAsSpyKs()

        val player1 = game.getFirstPlayer()
        val player2 = game.getNextPlayer(player1)

        // when

        game.play()

        // then

        val observer = game.getEventQueueTestObserver()
        observer.assertNoErrors()
        observer.assertComplete()

        observer.getValue(StartedNewGame::class).also {
            it.cardsInDeck shouldBeExactly 20
            it.playersCnt shouldBeExactly 2
            it.players shouldContainExactly listOf(player1, player2)
        }

        observer.getValues(StartedNewRound::class, 3)

        observer.getValues(AppointedFirstPlayer::class, 3)

        observer.getValues(AppointedNextPlayer::class, 3, true)

        observer.getAnyOfValues(
            listOf(
                WonRoundCauseOneAlive::class,
                WonRoundCauseEscaped::class,
                WonRoundCauseDeckOver::class
            ),
            3
        )

        observer.getValue(AnnouncedGameWinners::class).also {
            it.players shouldContainExactly listOf(player1, player2)
            it.winners shouldNot beEmpty()
        }
    }
})
