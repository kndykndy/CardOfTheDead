package cardofthedead.cards

import cardofthedead.game.Game
import cardofthedead.players.Player

abstract class Card(
    val game: Game,
    val title: String,
    val description: String
) {

    open fun play(playedBy: Player) {
        throw java.lang.IllegalStateException("${this.title} cannot be played directly.")
    }
}

abstract class Action(
    game: Game,
    name: String,
    description: String,
    val movementPoints: Int = 1
) : Card(game, name, description)

abstract class Event(
    game: Game,
    name: String,
    description: String
) : Card(game, name, description)

abstract class Zombie(
    game: Game,
    name: String,
    description: String = "",
    val zombiesOnCard: Int = 1
) : Card(game, name, description)

class PlayCardDecision(
    val wayToPlayCard: WayToPlayCard,
    val card: Card? = null
) {

    fun isGonnaPlay(): Boolean =
        wayToPlayCard != WayToPlayCard.CANNOT_PLAY &&
                wayToPlayCard != WayToPlayCard.DO_NOT_PLAY

    override fun toString(): String =
        when (wayToPlayCard) {
            WayToPlayCard.CANNOT_PLAY -> "Cannot play"
            WayToPlayCard.DO_NOT_PLAY -> "Do not play"
            WayToPlayCard.PLAY_AS_ACTION -> "Play ${card?.title} as action."
            WayToPlayCard.PLAY_AS_MOVEMENT_POINTS -> "Play ${card?.title} as movement points."
        }

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
