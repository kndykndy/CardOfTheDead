package cardofthedead

import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
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

    game.eventsQueue
        .ofType(MessagesFacade.Game.General.NewGame::class.java)
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

    game.eventsQueue.ofType(MessagesFacade.Game.Pending.NewRound::class.java)
        .subscribe { msg -> println("Starting round #${msg.roundIdx}") }

    game.eventsQueue
        .ofType(MessagesFacade.Game.General.WinnersAnnouncement::class.java)
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

//    MessagesFacade.Game.Pending.FirstPlayer -> println("${currentPlayer.name}'s starting!")
//    MessagesFacade.Game.Pending.NextPlayer -> println("${currentPlayer.name}'s turn now!")

    game.play()
}
