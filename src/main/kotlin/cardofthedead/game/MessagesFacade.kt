package cardofthedead.game

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
        }
    }
}
