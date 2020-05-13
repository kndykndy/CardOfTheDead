package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.Event
import cardofthedead.cards.Zombie
import cardofthedead.game.EventsFacade.Game.ActionCards.PlayedBarricade
import cardofthedead.game.Game
import cardofthedead.players.Player

class Barricade(
    game: Game
) : Action(
    game,
    "Barricade",
    "draw three cards. choose one and put it on the bottom of the deck"
) {

    /**
     * Draw three cards. Choose one and put it on the bottom of the deck.
     */
    override fun play(playedBy: Player) {
        playedBy.pickCandidateCards(3)
        playedBy
            .chooseWorstCandidateForBarricade()
            ?.let(game.playDeck::addCardOnBottom)

        var tookCardsToHand = 0

        repeat(2) {
            when (val candidateTopHand = playedBy.candidatesToHand.pickTopCard()) {
                is Action -> {
                    playedBy.takeToHand(candidateTopHand)
                    tookCardsToHand++
                }
                is Zombie -> playedBy.chasedByZombie(candidateTopHand)
                is Event -> playedBy.play(candidateTopHand)
            }
        }

        playedBy.publishEvent(PlayedBarricade(playedBy, tookCardsToHand))
    }
}
