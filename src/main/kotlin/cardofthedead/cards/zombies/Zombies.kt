package cardofthedead.cards.zombies

import cardofthedead.cards.Zombie
import cardofthedead.game.Game

class Zombies(
    game: Game
) : Zombie(
    game,
    "Zombies",
    "Counts as two zombie cards. " +
            "Cannot be discarded with Slugger.",
    2
)
