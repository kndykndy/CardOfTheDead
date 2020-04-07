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

            data class StartedNewRound(val roundIdx: Int) : Message

            data class ChoseFirstPlayer(val player: Player) : Message
            data class ChoseNextPlayer(val player: Player) : Message

            data class DrewAction(val player: Player, val action: Action) : Message
            data class DrewZombie(val player: Player, val zombie: Zombie) : Message
            data class DrewEvent(val player: Player, val event: Event) : Message
            data class DrewNoCard(val player: Player, val reason: DrewNoCardReason) : Message

            data class DecidedToPlayFromHand(val player: Player, val card: Card) : Message
            data class DecidedToPlayFromHandAsMp(val player: Player, val card: Card) : Message
            data class DecidedNotToPlayFromHand(val player: Player) : Message

            data class Died(val player: Player) : Message

            data class WonRoundCauseOneAlive(val player: Player) : Message
            data class WonRoundCauseEscaped(val player: Player) : Message
            data class WonRoundCauseDeckOver(val players: List<Player>) : Message
        }

        class ActionCards {
            data class PlayedArmored(val player: Player, val putBittenOnBottom: Boolean) : Message
            data class PlayedBarricade(val player: Player, val tookToHandEventually: Int) : Message
            data class PlayedChainsaw(
                val player: Player,
                val discardedZombies: MutableList<Zombie>
            ) : Message

            data class PlayDynamite(
                val player: Player,
                val discardedZombies: MutableList<Zombie>,
                val discardedMovementCard: Action?
            ) : Message
        }
    }
}
