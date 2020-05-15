package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.Zombie
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedSlugger
import cardofthedead.game.Game
import cardofthedead.players.Player

class Slugger(
    game: Game
) : Action(
    game,
    "Slugger",
    "Discard a zombie or take a card from another players hand."
) {

    override fun play(playedBy: Player) {
        if (playedBy.decideToDiscardZombieOrTakeCardForSlugger()) { // discard a zombie
            var discardedZombie: Zombie? = null

            val zombiesAround = playedBy.zombiesAround
            val singleZombies = zombiesAround.getSingleZombies()
            if (singleZombies.isNotEmpty()) {
                zombiesAround
                    .pickCard(singleZombies[playedBy.throwDice(singleZombies)])
                    ?.let {
                        playedBy.discard(it)
                        discardedZombie = it
                    }
            }

            playedBy.publishEvent(PlayedSlugger(playedBy, discardedZombie))
        } else { // take a card from player
            var tookCard: Action? = null

            val playerToTakeCardFrom = playedBy.choosePlayerToTakeCardFromForSlugger()
            playerToTakeCardFrom.hand
                .pickCard(playerToTakeCardFrom.throwDiceForHand())
                ?.let {
                    playedBy.takeToHand(it)
                    tookCard = it as Action
                }

            playedBy.publishEvent(PlayedSlugger(playedBy, null, tookCard, playerToTakeCardFrom))
        }
    }
}
