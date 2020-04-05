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

//    MessagesFacade.Game.Pending.RoundWinnerOneAlive ->
//    println("Oops... This round is over because ${winner.name} is the only player left alive!")
//    MessagesFacade.Game.Pending.RoundWinnerEscaped ->
//    println("Oops... This round is over because ${first.name} escaped!")
//    MessagesFacade.Game.Pending.RoundWinnersDeckOver ->
//    println(
//        "Oops... This round is over because there's no cards left in the deck! " +
//                "Winners are: ${winnersList.joinToString { it.name }}."
//    )

//    MessagesFacade.Game.Pending.DrewAction =
//        println("${currentPlayer.name} draws card to hand.")
//    MessagesFacade.Game.Pending.DrewZombie =
//        println("${this.name} is chased by ${getZombiesAroundCount()} zombies now.")
//    MessagesFacade.Game.Pending.DrewEvent =
//        todo
//    MessagesFacade.Game.Pending.DrewNoCard =
//        println("${this.name} uses their chance not to draw a card this turn.")

//    MessagesFacade.Game.Pending.Dead =
//        println("Oops... ${this.name} was eaten by zombies.")

//    MessagesFacade.Game.Pending.DecisionToPlayFromHand =
//        println("${currentPlayer.name} decided to play ${actionCardFromHand::class.simpleName}.")
//    MessagesFacade.Game.Pending.DecisionToPlayFromHandAsMp =
//        println("${currentPlayer.name} decided to play ${actionCardFromHand::class.simpleName} as movement points.")
//        println("${this.name} has ${getMovementPoints()} movement points now.")
//    MessagesFacade.Game.Pending.DecisionNotToPlayFromHand =
//        println("${currentPlayer.name} decided not to play card from hand.")

    game.play()
}
