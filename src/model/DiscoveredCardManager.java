package model;

import cards.Card;

import java.util.ArrayList;

public class DiscoveredCardManager {

    private ArrayList<Card> undiscoveredCard = new ArrayList<>();


    public boolean checkUndiscovered(Card card) {
        if (undiscoveredCard.isEmpty()) return false;
        for (Card c : undiscoveredCard) {
            if (c == card) return true;
        }
        return false;
    }

    public void markUndiscovered(Card card) {
        if (!checkUndiscovered(card))
            undiscoveredCard.add(card);
    }

    public void markDiscovered(Card card) {
        if (checkUndiscovered(card))
            undiscoveredCard.remove(undiscoveredCard.indexOf(card));
    }
}
