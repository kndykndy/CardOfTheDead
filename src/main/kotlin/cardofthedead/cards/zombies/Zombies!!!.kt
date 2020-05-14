package cardofthedead.cards.zombies

import cardofthedead.cards.Zombie
import cardofthedead.game.Game

@Suppress("ClassName")
class `Zombies!!!`(
    game: Game
) : Zombie(
    game,
    "Zombies!!!",
    "Counts as three zombie cards. " +
            "Cannot be discarded with Slugger or Chainsaw.",
    3
)
