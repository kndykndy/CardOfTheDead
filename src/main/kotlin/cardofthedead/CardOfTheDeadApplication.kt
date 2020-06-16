package cardofthedead

import cardofthedead.decks.StandardDeck
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedArmored
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedBarricade
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedChainsaw
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedDynamite
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedHide
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedLure
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedNukes
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedPillage
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedSlugger
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedTripped
import cardofthedead.game.EventsFacade.Game.Amid.AppointedFirstPlayer
import cardofthedead.game.EventsFacade.Game.Amid.AppointedNextPlayer
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
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedCornered
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedFog
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedHorde
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedMobs
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedRingtone
import cardofthedead.game.EventsFacade.Game.Input.InputProvided
import cardofthedead.game.EventsFacade.Game.Input.InputRequested
import cardofthedead.game.Game
import cardofthedead.players.HumanPlayer
import cardofthedead.players.Level.EASY
import cardofthedead.players.Player
import cardofthedead.players.PlayerDescriptor
import cardofthedead.players.Sex.FEMALE
import cardofthedead.players.Sex.MALE
import cardofthedead.players.Sex.NONBINARY
import cardofthedead.players.getPronoun
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.math.min

fun main() {
    val game = Game.Builder(
        PlayerDescriptor("Luke"),
        PlayerDescriptor("Yoda", EASY, NONBINARY),
        StandardDeck()
    )
        .withPlayer(PlayerDescriptor("Rei", EASY, FEMALE))
        .withHumanPlayer("Gene", MALE)
        .build()

    game.getEventQueue()
        .ofType(StartedNewGame::class.java)
        .subscribeOn(Schedulers.io())
        .subscribe { msg ->
            println("Starting a new Game of the Dead!")

            println("${msg.cardsInDeck} cards in deck.")

            println(
                "Tonight we're having ${msg.playersCnt} players: " +
                        "${msg.players.joinToString { it.name }}."
            )
            println(
                "${msg.startingPlayer.name} was the last who went to shopping mall, " +
                        "${msg.startingPlayer.getPronoun()}'s starting the game!"
            )
        }
    game.getEventQueue()
        .ofType(AnnouncedGameWinners::class.java)
        .subscribe { msg ->
            println()
            println("The game is over!")
            printScores(msg.players)

            if (msg.winners.size == 1) {
                val winner = msg.winners.first()
                println("The winner is ${winner.name + " (${winner.getSurvivalPoints()})"}.")
            } else {
                println(
                    "The winners are " +
                            msg.winners.joinToString { it.name + " (${it.getSurvivalPoints()})." }
                )
            }
        }

    game.getEventQueue()
        .ofType(StartedNewRound::class.java)
        .subscribeOn(Schedulers.io())
        .subscribe { msg ->
            println()
            println("Starting round #${msg.withNumber}!")
            println()
        }

    game.getEventQueue()
        .ofType(AppointedFirstPlayer::class.java)
        .subscribe { msg ->
            println()
            println("${msg.player.name}'s starting (#1)!")
        }
    game.getEventQueue()
        .ofType(AppointedNextPlayer::class.java)
        .subscribe { msg ->
            println()
            println("${msg.player.name}'s turn (#${msg.turnNumber})!")

            if (msg.player is HumanPlayer) {
                println("Your hand: ${msg.player.hand}")
                println("Zombies around you: ${msg.player.zombiesAround}")
                println("Your escape cards: ${msg.player.escapeCards}")
            }
        }

    game.getEventQueue()
        .ofType(InputRequested::class.java)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .subscribe { msg ->
            if (msg.maxOptions == 1)
                println("  ${msg.player.name}, ${msg.inputTitle}:")
            else
                println("  ${msg.player.name}, ${msg.inputTitle}, max ${msg.maxOptions} options:")
            msg.inputOptions.forEach { println("    $it") }

            while (true) {
                if (msg.maxOptions == 1)
                    print("  Type option number: ")
                else
                    print("  Type option numbers separated by commas: ")

                val curVal = readLine() ?: ""

                try {
                    val inputValues = curVal.split(',')
                        .map { it.trim() }
                        .mapNotNull { it.toIntOrNull() }
                        .toSet()

                    if (inputValues.isEmpty())
                        throw IllegalArgumentException("Empty input is not allowed.")
                    else if (inputValues.size != min(msg.maxOptions, msg.inputOptions.size))
                        throw IllegalArgumentException(
                            "You entered not the requested amount of options."
                        )

                    inputValues
                        .map { msg.inputOptions.first { inputOption -> inputOption.idx == it } }

                    msg.player.publishInputEvent(InputProvided(inputValues))

                    break
                } catch (nfe: NumberFormatException) {
                    println("  \"${curVal}\" is an incorrect input.")
                } catch (nsee: NoSuchElementException) {
                    println("  \"${curVal}\" is an incorrect input.")
                } catch (iae: IllegalArgumentException) {
                    println("  " + iae.message)
                }
            }
        }

    game.getEventQueue()
        .ofType(DrewAction::class.java)
        .subscribe { msg -> println("${msg.player.name} draws Action to hand.") }
    game.getEventQueue()
        .ofType(DrewZombie::class.java)
        .subscribe { msg ->
            print("${msg.player.name} draws ${msg.drewZombie.title}. ")
            println(
                "${msg.player.getPronoun().capitalize()}'s chased by " +
                        "${msg.player.getZombiesAroundCount()} zombies now."
            )
        }
    game.getEventQueue()
        .ofType(DrewEvent::class.java)
        .subscribe { msg -> print("${msg.player.name} draws Event. ") }
    game.getEventQueue()
        .ofType(DrewNoCard::class.java)
        .subscribe { msg ->
            println("${msg.player.name} uses their chance not to draw a card this turn.")
        }

    game.getEventQueue()
        .ofType(DecidedToPlayFromHand::class.java)
        .subscribe { msg ->
            println("${msg.player.name} decides to play ${msg.card.title}.")
        }
    game.getEventQueue()
        .ofType(DecidedToPlayFromHandAsMp::class.java)
        .subscribe { msg ->
            print(
                "${msg.player.name} decides to play ${msg.card.title} as Movement Points, "
            )
            println(
                "now ${msg.player.getPronoun()} has " +
                        "${msg.player.getMovementPoints()} MP total."
            )
        }
    game.getEventQueue()
        .ofType(DecidedNotToPlayFromHand::class.java)
        .subscribe { msg ->
            println("${msg.player.name} decides not to play card from hand.")
        }

    game.getEventQueue()
        .ofType(Died::class.java)
        .subscribe { msg ->
            println("Oops... ${msg.player.name} was eaten by zombies.")
        }

    game.getEventQueue()
        .ofType(WonRoundCauseOneAlive::class.java)
        .subscribe { msg ->
            println()
            println("This round is over as ${msg.winner.name}'s the only player left alive!")
            printScores(msg.players)
        }
    game.getEventQueue()
        .ofType(WonRoundCauseEscaped::class.java)
        .subscribe { msg ->
            println()
            println("This round is over as ${msg.winner.name} escaped!")
            printScores(msg.players)
        }
    game.getEventQueue()
        .ofType(WonRoundCauseDeckOver::class.java)
        .subscribe { msg ->
            println()
            println("This round is over as there's no cards left in the deck!")
            printScores(msg.players)
            if (msg.winners.size == 1) {
                println("${msg.winners[0].name} is the winner!")
            } else {
                println("Winners are: ${msg.winners.joinToString { it.name }}.")
            }
        }

    game.getEventQueue()
        .ofType(PlayedArmored::class.java)
        .subscribe { msg ->
            val playerPronoun = msg.player.getPronoun().capitalize()

            print("${msg.player.name} plays Armored")
            if (msg.putBittenOnBottom) {
                println(" and puts Bitten on the bottom of the deck.")
            } else {
                println(". $playerPronoun does not have Bitten to put it on the bottom of the deck.")
            }
        }
    game.getEventQueue()
        .ofType(PlayedBarricade::class.java)
        .subscribe { msg ->
            print("${msg.player.name} plays Barricade")
            println(" and decides to take ${msg.tookCardsToHand} cards to hand.")
        }
    game.getEventQueue()
        .ofType(PlayedChainsaw::class.java)
        .subscribe { msg ->
            print("${msg.player.name} plays Chainsaw")
            if (msg.discardedZombies.isNotEmpty()) {
                val zombies = msg.discardedZombies.joinToString { it.title }
                val zombiesAroundCount = msg.player.getZombiesAroundCount()
                val zombieSituation =
                    if (zombiesAroundCount == 0) "not followed by"
                    else "followed by $zombiesAroundCount"

                println(" and discards $zombies and now is $zombieSituation zombies!")
            } else {
                println(" and discards no zombies.")
            }
        }
    game.getEventQueue()
        .ofType(PlayedDynamite::class.java)
        .subscribe { msg ->
            val discardCard =
                if (msg.andDiscardedMovementCard != null) msg.andDiscardedMovementCard.title
                else "no"

            print("${msg.player.name} plays Dynamite")
            if (msg.discardedZombies.isNotEmpty()) {
                val zombies = msg.discardedZombies.joinToString { it.title }
                val zombiesAroundCount = msg.player.getZombiesAroundCount()
                val decision =
                    if (zombiesAroundCount == 0) "not followed by"
                    else "followed by $zombiesAroundCount"

                println(
                    " and discards $zombies and $discardCard movement card " +
                            "and now is $decision zombies!"
                )
            } else {
                println(" and discards no zombies but $discardCard movement card.")
            }
        }
    game.getEventQueue()
        .ofType(PlayedHide::class.java)
        .subscribe { msg ->
            val decision =
                if (msg.andDecidedToDrawNoCardsNextTurn) "will draw"
                else "decides not to draw"

            print("${msg.player.name} plays Hide")
            if (msg.gaveZombie != null && msg.toPlayer != null) {
                println(
                    " and gives ${msg.gaveZombie.title} " +
                            "to the next player, ${msg.toPlayer.name}, " +
                            "and $decision cards next turn."
                )
            } else {
                println(" and gives no zombies to anyone and $decision cards next turn.")
            }
        }
    game.getEventQueue()
        .ofType(PlayedLure::class.java)
        .subscribe { msg ->
            print("${msg.player.name} plays Lure")
            if (msg.gaveZombie != null && msg.toPlayer != null) {
                println(" and gives ${msg.gaveZombie.title} to ${msg.toPlayer.name}.")
            } else {
                println(" and gives no zombies to anyone.")
            }
        }
    game.getEventQueue()
        .ofType(PlayedNukes::class.java)
        .subscribe { msg ->
            print("${msg.player.name} plays Nukes! ")
            println(
                "It discards all zombie cards and all cards in hand from all players, " +
                        "even from ${msg.player.name} themself!"
            )
        }
    game.getEventQueue()
        .ofType(PlayedPillage::class.java)
        .subscribe { msg ->
            print("${msg.player.name} plays Pillage")
            println(" and takes ${msg.pillagedCards.size} cards from other players.")
        }
    game.getEventQueue()
        .ofType(PlayedSlugger::class.java)
        .subscribe { msg ->
            print("${msg.player.name} plays Slugger")
            if (msg.discardedZombie != null) {
                println(" and discards ${msg.discardedZombie.title}.")
            } else {
                println(" and takes a card from ${msg.fromPlayer?.name}.")
            }
        }
    game.getEventQueue()
        .ofType(PlayedTripped::class.java)
        .subscribe { msg ->
            val mpSum = msg.discardedMovementCards.map { it.movementPoints }.sum()

            print("${msg.player.name} plays Tripped")
            println(
                " and discards their ${msg.discardedMovementCards.size} " +
                        "latest movement cards for $mpSum points total."
            )
        }

    game.getEventQueue()
        .ofType(PlayedCornered::class.java)
        .subscribe { msg ->
            print("${msg.player.name} got Cornered. ")
            println("All their movement cards are discarded!")
        }
    game.getEventQueue()
        .ofType(PlayedFog::class.java)
        .subscribe { msg ->
            print("${msg.player.name} got into Fog. ")
            val zombieSituation = msg.newPlayersToZombiesAroundMap.entries.joinToString {
                "${it.key.name} by ${it.value}"
            }
            println("Players have exchanged cards and now are chased by zombies: $zombieSituation.")
        }
    game.getEventQueue()
        .ofType(PlayedHorde::class.java)
        .subscribe { msg ->
            print("${msg.player.name} encounters Horde. ")
            println("Every survivor draws two cards in their turn till the end of a round.")
        }
    game.getEventQueue()
        .ofType(PlayedMobs::class.java)
        .subscribe { msg ->
            print("${msg.player.name} got off on Mobs. ")
            for (entry in msg.playersWereMobbedMap.entries) {
                if (!entry.value) {
                    println("${entry.key.name} had Slugger and Mobs went for the next player.")
                } else {
                    println("${entry.key.name} had no Slugger and was mobbed.")
                    break
                }
            }
        }
    game.getEventQueue()
        .ofType(PlayedRingtone::class.java)
        .subscribe { msg ->
            val playerPronoun = msg.player.getPronoun().capitalize()

            print("${msg.player.name} has a loud Ringtone. ")
            if (msg.hadZombiesAround != msg.nowHasZombiesAround) {
                println(
                    "$playerPronoun used to be chased by ${msg.hadZombiesAround} zombies " +
                            "and now there're ${msg.nowHasZombiesAround} zombies around!"
                )
            } else {
                println(
                    "$playerPronoun was so lucky they are still chased by " +
                            "${msg.player.getPronoun()} zombies!"
                )
            }
        }

    game.play()
//    while(game.isRunning()){
//        Thread.sleep(100L)
//    }
}

fun printScores(players: List<Player>) {
    val score = players.joinToString { it.name + " (${it.getSurvivalPoints()})" }
    println("Score: $score.")
}
