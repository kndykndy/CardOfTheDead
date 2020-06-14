package cardofthedead.game

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.Zombie
import cardofthedead.players.HumanPlayer
import cardofthedead.players.InputOption
import cardofthedead.players.Player

class EventsFacade {

    interface Event

    class Game {

        class Around {

            data class StartedNewGame(
                val cardsInDeck: Int,
                val playersCnt: Int,
                val players: List<Player>,
                val startingPlayer: Player
            ) : Event

            data class AnnouncedGameWinners(
                val players: List<Player>,
                val winners: List<Player>
            ) : Event
        }

        class Input {

            data class InputRequested(
                val player: HumanPlayer,
                val inputOptions: Set<InputOption>,
                val maxOptions: Int,
                val inputTitle: String
            ) : Event

            data class InputProvided(val selectedOptions: Set<Int>)
        }

        class Amid {

            enum class DrewNoCardReason { DeckIsEmpty, DecidedNotToDraw }

            data class StartedNewRound(val withNumber: Int) : Event

            data class AppointedFirstPlayer(val player: Player) : Event
            data class AppointedNextPlayer(val player: Player, val turnNumber: Int) : Event

            data class DrewAction(val player: Player, val drewAction: Action) : Event
            data class DrewZombie(val player: Player, val drewZombie: Zombie) : Event
            data class DrewEvent(val player: Player, val drewEvent: cardofthedead.cards.Event) :
                Event

            data class DrewNoCard(val player: Player, val forReason: DrewNoCardReason) : Event

            data class DecidedToPlayFromHand(val player: Player, val card: Card) : Event
            data class DecidedToPlayFromHandAsMp(val player: Player, val card: Card) : Event
            data class DecidedNotToPlayFromHand(val player: Player) : Event

            data class Died(val player: Player) : Event

            data class WonRoundCauseOneAlive(
                val winner: Player,
                val players: List<Player>
            ) : Event

            data class WonRoundCauseEscaped(
                val winner: Player,
                val players: List<Player>
            ) : Event

            data class WonRoundCauseDeckOver(
                val winners: List<Player>,
                val players: List<Player>
            ) : Event
        }

        class ActionCards {

            data class PlayedArmored(
                val player: Player,
                val putBittenOnBottom: Boolean
            ) : Event

            data class PlayedBarricade(
                val player: Player,
                val tookCardsToHand: Int
            ) : Event

            data class PlayedChainsaw(
                val player: Player,
                val discardedZombies: List<Zombie>
            ) : Event

            data class PlayedDynamite(
                val player: Player,
                val discardedZombies: List<Zombie>,
                val andDiscardedMovementCard: Action?
            ) : Event

            data class PlayedHide(
                val player: Player,
                val gaveZombie: Zombie?,
                val toPlayer: Player?,
                val andDecidedToDrawNoCardsNextTurn: Boolean
            ) : Event

            data class PlayedLure(
                val player: Player,
                val gaveZombie: Zombie?,
                val toPlayer: Player?
            ) : Event

            data class PlayedNukes(val player: Player) : Event

            data class PlayedPillage(
                val player: Player,
                val pillagedCards: List<Action>
            ) : Event

            data class PlayedSlugger(
                val player: Player,
                val discardedZombie: Zombie?,
                val orTookCard: Action? = null,
                val fromPlayer: Player? = null
            ) : Event

            data class PlayedTripped(
                val player: Player,
                val discardedMovementCards: List<Action>,
                val fromPlayer: Player
            ) : Event
        }

        class EventCards {

            data class PlayedCornered(val player: Player) : Event

            data class PlayedFog(
                val player: Player,
                val newPlayersToZombiesAroundMap: Map<Player, Int>
            ) : Event

            data class PlayedHorde(val player: Player) : Event

            data class PlayedMobs(
                val player: Player,
                val playersWereMobbedMap: Map<Player, Boolean>
            ) : Event

            data class PlayedRingtone(
                val player: Player,
                val hadZombiesAround: Int,
                val nowHasZombiesAround: Int
            ) : Event
        }
    }
}
