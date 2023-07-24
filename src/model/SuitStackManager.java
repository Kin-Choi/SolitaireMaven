package model;

import cards.Card;
import cards.Rank;

import java.util.Map;
import java.util.HashMap;

public class SuitStackManager {

    private final Map<SuitStack, Card> suitStackMap = new HashMap<>();

    // Enum representing the suit stacks
    public enum SuitStack implements Location {
        STACK_CLUBS, STACK_DIAMONDS, STACK_SPADES, STACK_HEARTS
    }

    // Method to add a card to the suit stack
    public void add(Card card) {
        assert card != null;
        if (!suitStackMap.containsKey(SuitStack.values()[card.getSuit().ordinal()])) {
            suitStackMap.put(SuitStack.values()[card.getSuit().ordinal()], card);
        } else {
            suitStackMap.replace(SuitStack.values()[card.getSuit().ordinal()], card);
        }
        checkIsWon();
    }

//    //For testing
//    public void checkIsWon() {
//        int finishedSuitStack = 0;
//        for (SuitStack ss : SuitStack.values()) {
//            Card card = viewSuitStack(ss);
//            if (card != null && card.getRank() == Rank.ACE) finishedSuitStack++;
//        }
//        if (finishedSuitStack == 1) GameModel.getInstance().setHasWon(true);
//    }

    public void checkIsWon() {
        int finishedSuitStack = 0;
        for (SuitStack ss : SuitStack.values()) {
            Card card = viewSuitStack(ss);
            if (card != null && card.getRank() == Rank.KING) finishedSuitStack++;
        }
        if (finishedSuitStack == 4) GameModel.getInstance().setHasWon(true);
    }



    public Card draw(SuitStack suitStack) {
        Card card = viewSuitStack(suitStack);
        assert !(card == null);
        if (card.getRank() == Rank.ACE) {
            suitStackMap.remove(suitStack, card);
            return card;
        } else {
            suitStackMap.replace(suitStack, new Card(Rank.values()[card.getRank().ordinal() - 1], card.getSuit()));
            return card;
        }
    }

    // Method to check if a suit stack can be drawn from
    public boolean canDraw(Location suitStack) {
        if (suitStackMap.containsKey(suitStack)) {
            return true;
        }
        return false;
    }

    // Method to check if a card can be added to the suit stack
    public boolean canAdd(Card card) {
        assert card != null;
        if (!suitStackMap.containsKey(SuitStack.values()[card.getSuit().ordinal()])) {
            if (card.getRank() == Rank.ACE) {
                return true;
            }
        } else {
            if (suitStackMap.get(SuitStack.values()[card.getSuit().ordinal()]).getRank().ordinal() + 1 == card.getRank().ordinal()) {
                return true;
            }
        }
        return false;
    }

    // Method to view the top card of a suit stack
    public Card viewSuitStack(SuitStack suitStack) {
        if (suitStackMap.containsKey(suitStack)) {
            return suitStackMap.get(suitStack);
        } else {
            return null;
        }
    }

}
