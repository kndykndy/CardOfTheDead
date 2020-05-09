package cardofthedead.cards.zombies

import cardofthedead.cards.Zombie
import cardofthedead.game.Game

class Zombies(
    game: Game
) : Zombie(
    game,
    "Zombies...",
    "counts as two zombie cards. " +
            "cannot be discarded with slugger",
    2
)
