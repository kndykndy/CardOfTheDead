package main.kotlin.cardofthedead.players

import main.kotlin.cardofthedead.cards.PlayCardDecision
import main.kotlin.cardofthedead.cards.WayToPlayCard
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
     * todo redo if -- two branches almost same
     */
    override fun decideToPlayCardFromHand(): PlayCardDecision {
        if (hand.isEmpty()) return PlayCardDecision.cannotPlay()

        val zombiesToSurround =
            gameContext.playersToZombiesToBeSurrounded.getValue(gameContext.initialPlayersCount)
        if (zombiesAround.getZombiesCount() < zombiesToSurround) {
            // can DO_NOT_PLAY / PLAY_AS_ACTION / PLAY_AS_MOVEMENT_POINTS
            val pickActionCards = hand.pickActionCards()
            return if (!pickActionCards.isEmpty()) {
                if (Random.nextBoolean()) {
                    if (Random.nextBoolean()) {
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
        } else {
            // can DO_NOT_PLAY / PLAY_AS_ACTION
            val pickActionCards = hand.pickActionCards()
            return if (!pickActionCards.isEmpty()) {
                if (Random.nextBoolean()) {
                    PlayCardDecision(
                        WayToPlayCard.PLAY_AS_ACTION,
                        pickActionCards.pickRandomCard()
                    )
                } else {
                    PlayCardDecision.doNotPlay()
                }
            } else {
                PlayCardDecision.cannotPlay()
            }
        }
    }
}
