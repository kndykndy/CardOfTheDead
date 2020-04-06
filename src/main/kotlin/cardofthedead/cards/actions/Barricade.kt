package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.cards.Event
import cardofthedead.cards.Zombie
import cardofthedead.game.Game
import cardofthedead.game.MessagesFacade
import cardofthedead.players.Player

class Barricade(gameContext: Game) : Action(gameContext, 1) {

    /**
     * Draw three cards. Choose one and put it on the bottom of the deck.
     */
    override fun play(playedBy: Player) {
        playedBy.pickCandidateCards(3)

        playedBy.chooseWorstCandidateForBarricade()
            ?.let(gameContext.playDeck::addCardOnBottom)

        var tookToHand = 0

        repeat(2) {
            when (val candidateTopHand = playedBy.candidatesToHand.pickTopCard()) {
                null -> return
                is Zombie -> playedBy.chasedByZombie(candidateTopHand)
                is Event -> playedBy.play(candidateTopHand)
                else -> {
                    playedBy.takeToHand(candidateTopHand)
                    tookToHand++
                }
            }
        }

        playedBy.events.onNext(
            MessagesFacade.Game.ActionCards.PlayedBarricade(playedBy, tookToHand)
        )
    }
}
