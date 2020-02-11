package main.kotlin.cardofthedead.cards

import main.kotlin.cardofthedead.players.Player

abstract class Zombie(
    val zombiesOnCard: Int
) : Card() {

    override fun play(
        player: Player,
        playDeck: Deck
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
