package cardofthedead.decks

import cardofthedead.cards.events.Cornered
import cardofthedead.cards.zombies.`Zombies!!!`
import cardofthedead.game.Game
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class StandardDeckTest : StringSpec({

    "should " {
        // given
        val game = Game()

        // when
        val deck = StandardDeck(game)

        // then

        deck.cards.size shouldBe 56
        deck.getActions() shouldHaveSize 27
        deck.hasCardOfClass(Cornered::class.java) shouldBe true
        deck.hasCardOfClass(`Zombies!!!`::class.java) shouldBe true
    }
})
