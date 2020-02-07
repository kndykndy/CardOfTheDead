package main.kotlin.cardofthedead.cards.zombies

import main.kotlin.cardofthedead.cards.Zombie

class `Zombies!!!` : Zombie(3) {

    companion object {

        const val CARDS_IN_FULL_DECK: Int = 1
        const val DESCRIPTION =
            "counts as three zombie cards. cannot be discarded with slugger or chainsaw"
    }
}
