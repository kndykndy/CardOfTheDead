package cardofthedead

import cardofthedead.cards.Card
import cardofthedead.cards.Deck
import cardofthedead.players.Player

fun Deck<Card>.addCards(listOfCards: List<Card>) =
    listOfCards.forEach { this.addCard(it) }

fun Player.addCardsToCandidates(listOfCards: List<Card>) =
    listOfCards.forEach { this.candidatesToHand.addCard(it) }
