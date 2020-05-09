package cardofthedead.cards.zombies

import cardofthedead.cards.Zombie
import cardofthedead.game.Game

@Suppress("ClassName")
class `Zombies!!!`(
    game: Game
) : Zombie(
    game,
    "Zombies!!!",
    "counts as three zombie cards. " +
            "cannot be discarded with slugger or chainsaw",
    3
)
