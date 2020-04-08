package cardofthedead.game

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.Event
import cardofthedead.cards.Zombie
import cardofthedead.players.Player

class MessagesFacade {

    interface Message

    class Game {

        class Around {

            data class StartedNewGame(
                val cardsInDeck: Int,
                val playersCnt: Int,
                val players: List<Player>,
                val startingPlayer: Player
            ) : Message

            data class AnnouncedGameWinners(
                val players: List<Player>,
                val winners: List<Player>
            ) : Message
        }

        class Amid {

            enum class DrewNoCardReason { DeckIsEmpty, DecidedNotToDraw }

            data class StartedNewRound(val withNumber: Int) : Message

            data class ChoseFirstPlayer(val player: Player) : Message
            data class ChoseNextPlayer(val player: Player) : Message

            data class DrewAction(val player: Player, val drewAction: Action) : Message
            data class DrewZombie(val player: Player, val drewZombie: Zombie) : Message
            data class DrewEvent(val player: Player, val drewEvent: Event) : Message
            data class DrewNoCard(val player: Player, val forReason: DrewNoCardReason) : Message

            data class DecidedToPlayFromHand(val player: Player, val card: Card) : Message
            data class DecidedToPlayFromHandAsMp(val player: Player, val card: Card) : Message
            data class DecidedNotToPlayFromHand(val player: Player) : Message

            data class Died(val player: Player) : Message

            data class WonRoundCauseOneAlive(val player: Player) : Message
            data class WonRoundCauseEscaped(val player: Player) : Message
            data class WonRoundCauseDeckOver(val players: List<Player>) : Message
        }

        class ActionCards {

            data class PlayedArmored(
                val player: Player,
                val putBittenOnBottom: Boolean
            ) : Message

            data class PlayedBarricade(
                val player: Player,
                val tookCardsToHand: Int
            ) : Message

            data class PlayedChainsaw(
                val player: Player,
                val discardedZombies: List<Zombie>
            ) : Message

            data class PlayedDynamite(
                val player: Player,
                val discardedZombies: List<Zombie>,
                val andDiscardedMovementCard: Action?
            ) : Message

            data class PlayedHide(
                val player: Player,
                val gaveZombie: Zombie?,
                val toPlayer: Player?,
                val andDecidedToDrawNoCardsNextTurn: Boolean
            ) : Message

            data class PlayedLure(
                val player: Player,
                val gaveZombie: Zombie?,
                val toPlayer: Player?
            ) : Message

            data class PlayedNukes(val player: Player) : Message
            data class PlayedPillage(
                val player: Player,
                val pillagedCards: List<Action>
            ) : Message

            data class PlayedSlugger(
                val player: Player,
                val discardedZombie: Zombie?,
                val orTookCard: Action? = null,
                val fromPlayer: Player? = null
            ) : Message
        }
    }
}
