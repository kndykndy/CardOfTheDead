package cardofthedead.players

import cardofthedead.cards.Action
import cardofthedead.cards.Card
import cardofthedead.cards.PlayCardDecision
import cardofthedead.cards.WayToPlayCard
import cardofthedead.cards.actions.Armored
import cardofthedead.cards.actions.Barricade
import cardofthedead.cards.actions.Bitten
import cardofthedead.cards.actions.Chainsaw
import cardofthedead.cards.actions.Dynamite
import cardofthedead.cards.actions.Hide
import cardofthedead.cards.actions.Lure
import cardofthedead.cards.actions.Pillage
import cardofthedead.cards.actions.Slugger
import cardofthedead.cards.actions.Tripped
import cardofthedead.cards.actions.`Nukes!`
import cardofthedead.cards.events.Cornered
import cardofthedead.cards.events.Fog
import cardofthedead.cards.events.Horde
import cardofthedead.cards.events.Mobs
import cardofthedead.cards.events.Ringtone
import cardofthedead.cards.zombies.BrideZombie
import cardofthedead.cards.zombies.GrannyZombie
import cardofthedead.cards.zombies.LadZombie
import cardofthedead.cards.zombies.RedneckZombie
import cardofthedead.cards.zombies.Zombies
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.decks.getActions
import cardofthedead.decks.getSingleZombies
import cardofthedead.game.Game

class EasyPlayer(
    gameContext: Game,
    name: String,
    sex: Sex
) : Player(gameContext, name, sex) {

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
        if (n <= 0) return
        val actionCards = candidatesToHand.getActions().filter { it.movementPoints == 1 }
        if (actionCards.size > n) {
            actionCards.shuffled().take(n)
        } else {
            actionCards
        }.forEach { candidatesToHand.pickCard(it)?.let(hand::addCard) }
    }

    /**
     * If able to play, plays by throwing two dice.
     */
    override fun decideToPlayCardFromHand(): PlayCardDecision {
        if (hand.isEmpty()) return PlayCardDecision.cannotPlay()

        val playableActionCards = getPlayableActionCards()

        return if (playableActionCards.isNotEmpty()) {
            if (throwCoin()) {
                val zombiesToSurround =
                    gameContext.playersToZombiesToBeSurrounded.getValue(
                        gameContext.initialPlayersCount
                    )

                val notSurrounded = getZombiesAroundCount() < zombiesToSurround
                val lotsOfCardsOnHand = this.hand.size() > 3

                val playAsMovementPoints = (notSurrounded && throwCoin()) || lotsOfCardsOnHand
                PlayCardDecision(
                    if (playAsMovementPoints) WayToPlayCard.PLAY_AS_MOVEMENT_POINTS
                    else WayToPlayCard.PLAY_AS_ACTION,
                    hand.pickCard(playableActionCards.random())
                )
            } else {
                PlayCardDecision.doNotPlay()
            }
        } else {
            PlayCardDecision.cannotPlay()
        }
    }

    override fun chooseWorstCandidateForBarricade(): Card? =
        candidatesToHand.cards
            .map { Pair(it, cardTypeToRating.getValue(it::class)) }
            .minBy { it.second }
            ?.first
            ?.let(candidatesToHand::pickCard)

    override fun chooseWorstMovementCardForDynamite(): Action? =
        if (escapeCards.isNotEmpty()) {
            if (escapeCards.size() == 1) {
                escapeCards.pickTopCard()
            } else {
                escapeCards.pickRandomCard()
            }
        } else {
            null
        }

    override fun decideToDrawNoCardsNextTurnForHide() {
        doDrawCardThisTurn = true
    }

    override fun choosePlayerToGiveZombieToForLure(): Player = gameContext.getRandomPlayer(this)

    /**
     * @return true if decided to discard a zombie, false if take player's card.
     */
    override fun decideToDiscardZombieOrTakeCardForSlugger(): Boolean {
        val tooManyZombiesAround =
            getZombiesAroundCount() >
                    gameContext.playersToZombiesToBeSurrounded.getValue(
                        gameContext.initialPlayersCount
                    )

        val hasZombieToDiscard = zombiesAround.getSingleZombies().isNotEmpty()

        return tooManyZombiesAround && hasZombieToDiscard
    }

    override fun choosePlayerToTakeCardFromForSlugger(): Player =
        gameContext.getRandomPlayer(this)

    override fun choosePlayerToDiscardMovementCardsFromForTripped(): Player =
        gameContext.getRandomPlayer(this)

    override fun decideHowManyMovementCardsToDiscardForTripped(): Int = throwDice(2)

    private fun getPlayableActionCards(): List<Action> =
        hand.getActions().filterNot { it is Bitten }
}
