package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.cards.Zombie
import cardofthedead.game.EventsFacade.Game.EventCards.PlayedFog
import cardofthedead.game.Game
import cardofthedead.players.Player

class Fog(
    game: Game
) : Event(
    game,
    "Fog",
    "All survivors take their zombies at hand. " +
            "Turn by turn players draw a card from the player to the right. " +
            "And then once more. " +
            "Survivors show their zombies."
) {

    override fun play(playedBy: Player) {
        game.players
            .forEach { it.hand.merge(it.zombiesAround) }

        repeat(2) {
            var currentPlayer = playedBy
            do {
                val prevPlayer = game.getPrevPlayer(currentPlayer)
                prevPlayer.hand.pickCard(prevPlayer.throwDice(prevPlayer.hand))
                    ?.let(currentPlayer::takeToHand)

                currentPlayer = game.getNextPlayer(currentPlayer)
            } while (currentPlayer != playedBy)
        }

        game.players
            .forEach { player ->
                while (player.hand.hasCardOfClass(Zombie::class.java)) {
                    player.chasedByZombie(player.hand.pickCardOfClass(Zombie::class.java) as Zombie)
                }
            }

        playedBy.publishEvent(
            PlayedFog(
                playedBy,
                game.players
                    .map { it to it.getZombiesAroundCount() }
                    .toMap()
            )
        )
    }
}
