package cardofthedead.cards.actions

import cardofthedead.cards.Action
import cardofthedead.game.Game

/**
 * You're not getting survival points if this card's in your hand by the end of the round.
 * This card cannot be played as a movement card.
 *
 * This card cannot be played as Movement card.
 */
class Bitten(gameContext: Game) : Action(gameContext, 0)
