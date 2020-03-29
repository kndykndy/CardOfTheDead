package cardofthedead.cards

import cardofthedead.game.Game

abstract class Zombie(
    gameContext: Game,
    val zombiesOnCard: Int
) : Card(gameContext)
