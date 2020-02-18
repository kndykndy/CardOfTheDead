package main.kotlin.cardofthedead.players

import main.kotlin.cardofthedead.cards.Card
import main.kotlin.cardofthedead.cards.PlayCardDecision
import main.kotlin.cardofthedead.cards.WayToPlayCard
import main.kotlin.cardofthedead.cards.actions.Armored
import main.kotlin.cardofthedead.cards.actions.Barricade
import main.kotlin.cardofthedead.cards.actions.Bitten
import main.kotlin.cardofthedead.cards.actions.Chainsaw
import main.kotlin.cardofthedead.cards.actions.Dynamite
import main.kotlin.cardofthedead.cards.actions.Hide
import main.kotlin.cardofthedead.cards.actions.Lure
import main.kotlin.cardofthedead.cards.actions.Pillage
import main.kotlin.cardofthedead.cards.actions.Slugger
import main.kotlin.cardofthedead.cards.actions.Tripped
import main.kotlin.cardofthedead.cards.actions.`Nukes!`
import main.kotlin.cardofthedead.cards.events.Cornered
import main.kotlin.cardofthedead.cards.events.Fog
import main.kotlin.cardofthedead.cards.events.Horde
import main.kotlin.cardofthedead.cards.events.Mobs
import main.kotlin.cardofthedead.cards.events.Ringtone
import main.kotlin.cardofthedead.cards.zombies.BrideZombie
import main.kotlin.cardofthedead.cards.zombies.GrannyZombie
import main.kotlin.cardofthedead.cards.zombies.LadZombie
import main.kotlin.cardofthedead.cards.zombies.RedneckZombie
import main.kotlin.cardofthedead.cards.zombies.Zombies
import main.kotlin.cardofthedead.cards.zombies.`Zombies!!!`
import kotlin.random.Random

class EasyPlayer(name: String) : Player(name) {

    /**
     * Worst cards rating. The bigger the better.
     */
    private val cardTypeToRating = mapOf(

        Bitten::class to 0,
        `Zombies!!!`::class to 1,
        Zombies::class to 2,
        BrideZombie::class to 3,
        GrannyZombie::class to 3,
        LadZombie::class to 3,
        RedneckZombie::class to 3,
        Cornered::class to 4,
        Ringtone::class to 5,
        Mobs::class to 6,
        Fog::class to 7,
        Horde::class to 7,
        Armored::class to 8,
        Barricade::class to 8,
        Chainsaw::class to 8,
        Hide::class to 8,
        Lure::class to 8,
        Slugger::class to 8,
        Tripped::class to 8,
        Dynamite::class to 9,
        Pillage::class to 9,
        `Nukes!`::class to 9
    )

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

    override fun chooseWorstCandidateForBarricade(): Card? =
        candidatesToHand.getInnerCards()
            .map { Pair(it, cardTypeToRating[it::class]) }
            .sortedBy { it.second }
            .first()
            .first

    override fun chooseWorstMovementCardForDynamite(): Card? {
        return if (!escapeCards.isEmpty()) {
            if (escapeCards.size() == 1) {

            } else {
                escapeCards.getInnerCards().reversed().forEach {

                }
            }

        } else {
            null
        }
    }
}
