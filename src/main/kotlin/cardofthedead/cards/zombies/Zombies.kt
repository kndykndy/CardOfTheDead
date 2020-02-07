package main.kotlin.cardofthedead.cards.zombies

import main.kotlin.cardofthedead.cards.Zombie

class Zombies : Zombie(2) {

    companion object {

        const val CARDS_IN_FULL_DECK: Int = 2
        const val DESCRIPTION = "counts as two zombie cards. cannot be discarded with slugger"
    }
}
