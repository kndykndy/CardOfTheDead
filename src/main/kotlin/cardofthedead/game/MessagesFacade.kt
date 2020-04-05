package cardofthedead.game

import cardofthedead.cards.Action
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

            data class NewRound(val roundIdx: Int) : Message

            data class FirstPlayer(val player: Player) : Message
            data class NextPlayer(val player: Player) : Message

            data class DrawAction(val action: Action) : Message
            data class DrawZombie(val zombie: Zombie) : Message
            data class DrawEvent(val event: Event) : Message

            data class RoundWinnerOneAlive(val player: Player) : Message
            data class RoundWinnerEscaped(val player: Player) : Message
            data class RoundWinnersDeckOver(val players: List<Player>) : Message
        }
    }
}
