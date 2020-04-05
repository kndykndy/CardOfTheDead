package cardofthedead.game

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.Event
import cardofthedead.cards.Zombie
import cardofthedead.players.Player

class MessagesFacade {

    interface Message

    class Game {

        class General {

            data class NewGame(
                val cardsInDeck: Int,
                val playersCnt: Int,
                val players: List<Player>,
                val startingPlayer: Player
            ) : Message

            data class WinnersAnnouncement(
                val players: List<Player>,
                val winners: List<Player>
            ) : Message
        }

        class Pending {

            enum class DrewNoCardReason { DeckIsEmpty, DecidedNotToDraw }

            data class NewRound(val roundIdx: Int) : Message

            data class FirstPlayer(val player: Player) : Message
            data class NextPlayer(val player: Player) : Message

            data class DrewAction(val player: Player, val action: Action) : Message
            data class DrewZombie(val player: Player, val zombie: Zombie) : Message
            data class DrewEvent(val player: Player, val event: Event) : Message
            data class DrewNoCard(val player: Player, val reason: DrewNoCardReason) : Message

            data class DecisionToPlayFromHand(val player: Player, val card: Card) : Message
            data class DecisionToPlayFromHandAsMp(val player: Player, val card: Card) : Message
            data class DecisionNotToPlayFromHand(val player: Player) : Message

            data class RoundWinnerOneAlive(val player: Player) : Message
            data class RoundWinnerEscaped(val player: Player) : Message
            data class RoundWinnersDeckOver(val players: List<Player>) : Message
        }
    }
}
