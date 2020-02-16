package main.kotlin.cardofthedead.cards

import main.kotlin.cardofthedead.players.Player

abstract class Card {

    open fun play(player: Player, playDeck: Deck<Card>, discardDeck: Deck<Card>) {
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
