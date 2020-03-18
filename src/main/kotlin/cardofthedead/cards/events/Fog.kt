package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.cards.Zombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.players.Player

class Fog : Event() {

    /**
     * All survivors take their zombies at hand. Turn by turn players draw a card from the player
     * to the right. And then once more. Survivors show their zombies.
     */
    override fun play(playedBy: Player) {
        gameContext.players.forEach { player ->
            player.hand.merge(player.zombiesAround)
        }

        repeat(2) {
            var currentPlayer = playedBy
            do {
                val prevPlayer = gameContext.getPrevPlayer(currentPlayer)
                prevPlayer.hand.pickRandomCard()?.let { currentPlayer.hand.addCard(it) }

                currentPlayer = gameContext.getNextPlayer(currentPlayer)
            } while (currentPlayer != playedBy)
        }

        gameContext.players.forEach { player ->
            player.hand.pickCardOfClass(Zombies::class.java)?.let {
                player.chasedByZombie(playedBy.discard(it) as Zombie)
            }
        }
    }
}
