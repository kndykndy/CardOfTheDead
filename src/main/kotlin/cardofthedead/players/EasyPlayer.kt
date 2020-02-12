package main.kotlin.cardofthedead.players

import com.sun.tools.javac.jvm.ByteCodes.ret
import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.PlayCardDecision
import main.kotlin.cardofthedead.cards.WayToPlayCard
import main.kotlin.cardofthedead.cards.Zombie
import main.kotlin.cardofthedead.cards.zombies.Zombies
import kotlin.random.Random

class EasyPlayer(name: String) : Player(name) {

    /**
     * Picks random N cards from the candidates deck.
     */
    override fun chooseSinglePointCards(n: Int) {
        val actionCardsDeck = candidatesToHand.pickActionCards()
        if (actionCardsDeck.size() > n) {
            hand.merge(actionCardsDeck.pickRandomCards(n))
            candidatesToHand.merge(actionCardsDeck)
        } else {
            hand.merge(actionCardsDeck)
        }
    }

    /**
     * If able to play, plays by throwing two dice.
     */
    override fun decideToPlayCardFromHand(): PlayCardDecision {
        if (hand.isEmpty()) return PlayCardDecision.cannotPlay()

        val zombiesToSurround =
            gameContext.playersToZombiesToBeSurrounded.getValue(gameContext.initialPlayersCount)

        val notSurrounded = zombiesAround.getZombiesCount() < zombiesToSurround

        val pickActionCards = hand.pickActionCards()
        return if (!pickActionCards.isEmpty()) {
            if (Random.nextBoolean()) {
                if (notSurrounded && Random.nextBoolean()) {
                    PlayCardDecision(
                        WayToPlayCard.PLAY_AS_MOVEMENT_POINTS,
                        pickActionCards.pickRandomCard()
                    )
                } else {
                    PlayCardDecision(
                        WayToPlayCard.PLAY_AS_ACTION,
                        pickActionCards.pickRandomCard()
                    )
                }
            } else {
                PlayCardDecision.doNotPlay()
            }
        } else {
            PlayCardDecision.cannotPlay()
        }
    }

    override fun chooseWorstCandidateForBarricade(): Card? {
        // todo
//        val zombieCards = candidatesToHand.getCardsOfClass(Zombie::class)
//        if (zombieCards.isNotEmpty()) {
//            if(zombieCards.contains())
//            when(zombieCards.javaClass){
//                is Zombies::javaClass -> ret
//            }
//        }
    }
}
