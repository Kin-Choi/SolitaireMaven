package model;

import java.util.Stack;

import application.ScoreView;
import cards.Card;
import cards.Deck;
import cards.Rank;

public class WorkingStackManager {

    public enum Workingstack implements Location {
        STACK_ONE, STACK_TWO, STACK_THREE, STACK_FOUR, STACK_FIVE, STACK_SIX, STACK_SEVEN
    }

    private final WorkingStack[] workingStacks = new WorkingStack[Workingstack.values().length];

    // Constructor to initialize the working stacks with cards drawn from the deck
    public WorkingStackManager(Deck deck) {
        for (int i = 0; i < workingStacks.length; i++) {
            workingStacks[i] = new WorkingStack(deck, Workingstack.STACK_ONE.ordinal() + 1 + i);
        }
    }

    // Get the working stack at the specified index
    public Stack<Card> getWorkingStack(Workingstack index) {
        Stack<Card> stack = new Stack<>();
        for (Card card : this.workingStacks[index.ordinal()]) {
            stack.push(card);
        }
        return stack;
    }

    // Add a card to the specified working stack
    public void add(Card card, Workingstack index) {
        workingStacks[index.ordinal()].push(card);
    }

    // Check if it is possible to add a card to the specified working stack
    public boolean canAdd(Card card, Workingstack index) {
        assert card != null;
        if (workingStacks[index.ordinal()].isEmpty() && card.getRank() == Rank.KING) {
            return true;
        } else {
            // Check if the card's suit and rank meet the conditions for adding to the working stack
            if ((card.getSuit().ordinal() + workingStacks[index.ordinal()].peek().getSuit().ordinal()) % 2 != 0) {
                if (card.getRank().ordinal() == workingStacks[index.ordinal()].peek().getRank().ordinal() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    // Draw a card from the specified working stack
    public Card draw(Workingstack index) {
        Card card = workingStacks[index.ordinal()].draw();
        if (!workingStacks[index.ordinal()].isEmpty()) {
            GameModel.getInstance().markDiscovered(workingStacks[index.ordinal()].peek());
        }
        return card;
    }


    // Get the cards in the specified working stack
    public Stack<Card> getCards(Workingstack index) {
        Stack<Card> stack = new Stack<>();
        for (Card card : workingStacks[index.ordinal()]) {
            stack.push(card);
        }
        return stack;
    }

    // Check if it is possible to draw a card from the specified working stack
    public boolean canDraw(Workingstack index) {
        return !workingStacks[index.ordinal()].isEmpty();
    }

    // Add multiple cards to the specified working stack
    public void addMultiple(Stack<Card> stack, Workingstack index) {
        assert canAdd(stack.lastElement(), index);
        while (!stack.isEmpty()) {
            workingStacks[index.ordinal()].push(stack.pop());
        }
    }

    // Draw multiple cards from the specified working stack
    public Stack<Card> drawMultiple(Card card, Workingstack index) {
        assert canDraw(index);
        Stack<Card> stack = new Stack<>();
        while (!workingStacks[index.ordinal()].isEmpty() && card != workingStacks[index.ordinal()].peek())
        {
            stack.push(workingStacks[index.ordinal()].draw());
        }
        if (!workingStacks[index.ordinal()].isEmpty()) {
            stack.push(workingStacks[index.ordinal()].draw());
            if (!workingStacks[index.ordinal()].isEmpty()) {
                GameModel.getInstance().markDiscovered(workingStacks[index.ordinal()].peek());

            }
        }
        return stack;
    }

}
