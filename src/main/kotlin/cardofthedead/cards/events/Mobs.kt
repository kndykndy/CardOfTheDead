package cardofthedead.cards.events

import cardofthedead.cards.Event
import cardofthedead.cards.actions.Slugger
import cardofthedead.players.Player

class Mobs : Event() {

    /**
     * If you have a slugger at hand, ignore the event and pass it to the next player to the left.
     * Otherwise put all your hand on the bottom of the deck.
     */
    override fun play(playedBy: Player) {
        val pickCardOfClass = playedBy.hand.pickCardOfClass(Slugger::class.java)
        if(pickCardOfClass!=null){

        } else {

        }
    }
}
