package cardofthedead

import cardofthedead.game.EventsFacade.Game.Amid.ChoseFirstPlayer
import cardofthedead.game.EventsFacade.Game.Amid.ChoseNextPlayer
import cardofthedead.game.EventsFacade.Game.Amid.DecidedNotToPlayFromHand
import cardofthedead.game.EventsFacade.Game.Amid.DecidedToPlayFromHand
import cardofthedead.game.EventsFacade.Game.Amid.DecidedToPlayFromHandAsMp
import cardofthedead.game.EventsFacade.Game.Amid.Died
import cardofthedead.game.EventsFacade.Game.Amid.DrewAction
import cardofthedead.game.EventsFacade.Game.Amid.DrewEvent
import cardofthedead.game.EventsFacade.Game.Amid.DrewNoCard
import cardofthedead.game.EventsFacade.Game.Amid.DrewZombie
import cardofthedead.game.EventsFacade.Game.Amid.StartedNewRound
import cardofthedead.game.EventsFacade.Game.Amid.WonRoundCauseDeckOver
import cardofthedead.game.EventsFacade.Game.Amid.WonRoundCauseEscaped
import cardofthedead.game.EventsFacade.Game.Amid.WonRoundCauseOneAlive
import cardofthedead.game.EventsFacade.Game.Around.AnnouncedGameWinners
import cardofthedead.game.EventsFacade.Game.Around.StartedNewGame
import cardofthedead.game.Game
import cardofthedead.players.Level
import cardofthedead.players.PlayerDescriptor
import cardofthedead.players.Sex
import cardofthedead.players.getPronoun

fun main() {
    val game = Game.Builder(
        PlayerDescriptor("Luke"),
        PlayerDescriptor("Yoda")
    )
        .withPlayer(PlayerDescriptor("Rei", Level.EASY, Sex.FEMALE))
        .build()

    game.getEventQueue()
        .ofType(StartedNewGame::class.java)
        .subscribe { msg ->
            println("Starting a new Game of the Dead!")

            println("${msg.cardsInDeck} cards in deck.")

            println(
                "Tonight we're having ${msg.playersCnt} players: " +
                        "${msg.players.joinToString { it.name }}."
            )
            println(
                "${msg.startingPlayer.name} was the last who went to shopping mall, " +
                        "${msg.startingPlayer.getPronoun()}'s starting!"
            )
        }

    game.getEventQueue()
        .ofType(AnnouncedGameWinners::class.java)
        .subscribe { msg ->
            println("Player scores: " +
                    msg.players.joinToString { it.name + " (${it.getSurvivalPoints()})" })

            if (msg.winners.size == 1) {
                val winner = msg.winners.first()
                println("The winner is ${winner.name + " (${winner.getSurvivalPoints()})"}")
            } else {
                println("The winners are " +
                        msg.winners.joinToString { it.name + " (${it.getSurvivalPoints()})" }
                )
            }
        }

    game.getEventQueue()
        .ofType(StartedNewRound::class.java)
        .subscribe { msg -> println("Starting round #${msg.withNumber}") }

    game.getEventQueue()
        .ofType(ChoseFirstPlayer::class.java)
        .subscribe { msg -> println("${currentPlayer.name}'s starting!") } // todo
    game.getEventQueue()
        .ofType(ChoseNextPlayer::class.java)
        .subscribe { msg -> println("${currentPlayer.name}'s turn now!") } // todo

    game.getEventQueue()
        .ofType(DrewAction::class.java)
        .subscribe { msg -> println("${currentPlayer.name} draws card to hand.") } // todo
    game.getEventQueue()
        .ofType(DrewZombie::class.java)
        .subscribe { msg ->
            println("${this.name} is chased by ${getZombiesAroundCount()} zombies now.")
        } // todo
    game.getEventQueue()
        .ofType(DrewEvent::class.java)
        .subscribe {} // todo
    game.getEventQueue()
        .ofType(DrewNoCard::class.java)
        .subscribe { msg ->
            println("${this.name} uses their chance not to draw a card this turn.")
        } // todo

    game.getEventQueue()
        .ofType(DecidedToPlayFromHand::class.java)
        .subscribe { msg ->
            println("${currentPlayer.name} decided to play ${actionCardFromHand::class.simpleName}.")
        } // todo
    game.getEventQueue()
        .ofType(DecidedToPlayFromHandAsMp::class.java)
        .subscribe { msg ->
            println("${currentPlayer.name} decided to play ${actionCardFromHand::class.simpleName} as movement points.")
            println("${this.name} has ${getMovementPoints()} movement points now.")
        } // todo
    game.getEventQueue()
        .ofType(DecidedNotToPlayFromHand::class.java)
        .subscribe { msg ->
            println("${currentPlayer.name} decided not to play card from hand.")
        } // todo

    game.getEventQueue()
        .ofType(Died::class.java)
        .subscribe { msg -> println("Oops... ${this.name} was eaten by zombies.") } // todo

    game.getEventQueue()
        .ofType(WonRoundCauseOneAlive::class.java)
        .subscribe { msg ->
            println("Oops... This round is over because ${winner.name} is the only player left alive!")
        } // todo
    game.getEventQueue()
        .ofType(WonRoundCauseEscaped::class.java)
        .subscribe { msg ->
            println("Oops... This round is over because ${first.name} escaped!")
        } // todo
    game.getEventQueue()
        .ofType(WonRoundCauseDeckOver::class.java)
        .subscribe { msg ->
            println(
                "Oops... This round is over because there's no cards left in the deck! " +
                        "Winners are: ${winnersList.joinToString { it.name }}."
            )
        } // todo


    game.getEventQueue()
        .ofType(::class.java)
        .subscribe {} // todo

    game.play()
}
