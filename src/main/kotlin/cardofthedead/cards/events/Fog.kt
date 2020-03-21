package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.cards.Zombie
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
                prevPlayer.hand.pickRandomCard()?.let(currentPlayer::takeToHand)

                currentPlayer = gameContext.getNextPlayer(currentPlayer)
            } while (currentPlayer != playedBy)
        }

        gameContext.players.forEach { player ->
            while (player.hand.hasCardOfClass(Zombie::class.java)) {
                player.chasedByZombie(player.hand.pickCardOfClass(Zombie::class.java) as Zombie)
            }
        }
    }
}
