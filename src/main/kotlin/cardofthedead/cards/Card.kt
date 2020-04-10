package cardofthedead.cards

import cardofthedead.game.Game
import cardofthedead.players.Player

abstract class Card(
    val gameContext: Game
) {

    open fun play(playedBy: Player) {
        throw java.lang.IllegalStateException(
            "${this::class.simpleName} cannot be played directly."
        )
    }
}

abstract class Action(
    gameContext: Game,
    val movementPoints: Int
) : Card(gameContext)

abstract class Event(gameContext: Game) : Card(gameContext)

abstract class Zombie(
    gameContext: Game,
    val zombiesOnCard: Int
) : Card(gameContext)

class PlayCardDecision(
    val wayToPlayCard: WayToPlayCard,
    val card: Card? = null
) {

    fun isGonnaPlay(): Boolean =
        wayToPlayCard != WayToPlayCard.CANNOT_PLAY &&
                wayToPlayCard != WayToPlayCard.DO_NOT_PLAY

    companion object {

        fun cannotPlay(): PlayCardDecision = PlayCardDecision(WayToPlayCard.CANNOT_PLAY)
        fun doNotPlay(): PlayCardDecision = PlayCardDecision(WayToPlayCard.DO_NOT_PLAY)
    }
}

enum class WayToPlayCard {
    CANNOT_PLAY,
    DO_NOT_PLAY,
    PLAY_AS_ACTION,
    PLAY_AS_MOVEMENT_POINTS
}
