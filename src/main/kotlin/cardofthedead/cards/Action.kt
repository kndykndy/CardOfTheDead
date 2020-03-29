package cardofthedead.cards

import cardofthedead.game.Game

abstract class Action(
    gameContext: Game,
    val movementPoints: Int
) : Card(gameContext)
