package cardofthedead.cards

import cardofthedead.game.Game
import cardofthedead.players.Player

abstract class Card {

    internal lateinit var gameContext: Game

    open fun play(player: Player) {
        throw IllegalStateException("${this::class.simpleName} cannot be played directly.")
    }
}

class PlayCardDecision(
    val wayToPlayCard: WayToPlayCard,
    val card: Card?
) {

    fun isGonnaPlay(): Boolean =
        wayToPlayCard != WayToPlayCard.CANNOT_PLAY &&
                wayToPlayCard != WayToPlayCard.DO_NOT_PLAY

    companion object {

        fun cannotPlay(): PlayCardDecision = PlayCardDecision(WayToPlayCard.CANNOT_PLAY, null)

        fun doNotPlay(): PlayCardDecision = PlayCardDecision(WayToPlayCard.DO_NOT_PLAY, null)
    }
}

enum class WayToPlayCard {
    CANNOT_PLAY,
    DO_NOT_PLAY,
    PLAY_AS_ACTION,
    PLAY_AS_MOVEMENT_POINTS
}
