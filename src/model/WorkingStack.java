package model;

import cards.Card;
import cards.Deck;

import java.util.Iterator;
import java.util.Stack;

public class WorkingStack implements Iterable<Card>{
    private final Stack<Card> workingStack = new Stack<>();

    // Constructor to initialize the working stack with cards drawn from the deck
    public WorkingStack(Deck deck, int num) {
        for (int i = 0; i < num; i++) {
            Card card = deck.draw();
            workingStack.add(card);
            if (i != num - 1) GameModel.getInstance().markUndiscovered(card);
        }
    }

    // Push a card onto the working stack
    public void push(Card card) {
        workingStack.push(card);
    }

    // Check if the working stack is empty
    public boolean isEmpty() {
        return workingStack.isEmpty();
    }

    // Peek at the top card of the working stack
    public Card peek() {
        assert !workingStack.isEmpty();
        return workingStack.peek();
    }

    // Draw a card from the top of the working stack
    public Card draw() {
        assert !workingStack.isEmpty();
        return workingStack.pop();
    }

    // Implement the Iterable interface to allow iterating over the cards in the working stack
    @Override
    public Iterator<Card> iterator() {
        return workingStack.iterator();
    }
} 
