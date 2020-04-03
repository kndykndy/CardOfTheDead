package cardofthedead.game

import cardofthedead.players.Player

class MessagesFacade {

    interface Message

    data class NewGameMessage(
        val cardsInDeck: Int,
        val playersCnt: Int,
        val players: List<Player>,
        val startingPlayer: Player
    ) : Message

    data class NewRoundMessage(val roundIdx: Int) : Message

    data class WinnersAnnouncementMessage(
        val players: List<Player>,
        val winners: List<Player>
    ) : Message
}
