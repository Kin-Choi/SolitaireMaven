package cards;

import java.util.Collections;
import java.util.Stack;

public class Deck {

    private final Stack<Card> cards = new Stack<>();   // Stack to store the cards in the deck

    public void reset() {
        cards.clear();

        // Iterate over each suit and rank to create all possible cards in the deck
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.push(Card.getCard(rank, suit));
            }
        }

        // Shuffle the cards in the deck
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return !this.cards.isEmpty();
    }

    public Card draw() {
        assert isEmpty();
        // Draw a card from the top of the deck (pop operation)
        return this.cards.pop();
    }

    public void transferAll(Stack<Card> cards) {
        while(!cards.isEmpty()) {
            this.cards.push(cards.pop());
        }
    }
}
