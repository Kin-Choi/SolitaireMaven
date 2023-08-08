package model;

import application.Rule;
import application.ScoreView;
import cards.Card;
import cards.Deck;
import cards.Mode;
import model.SuitStackManager.SuitStack;
import model.WorkingStackManager.Workingstack;
import move.DeckMove;
import move.Move;
import move.MultipleCardsMove;
import move.OneCardMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameModel {

    // Singleton instance of the GameModel
    private static final GameModel INSTANCE = new GameModel();
    private final Deck deck = new Deck();   // Deck of playing cards
    private Stack<Card> waste;       // Waste pile of discarded cards
    private final List<GameModelListener> listenerList = new ArrayList<>();   // List of listeners to the game state
    private WorkingStackManager workingStackManager;   // Manager for working stacks
    private SuitStackManager suitStackManager;         // Manager for suit stacks
    private DiscoveredCardManager discoveredCardManager;
    private boolean hasWon = false;
    private boolean lose = false;
    private boolean requireReset = false;
    private Mode mode = Mode.NORMAL;
    private Rule rule = new Rule(mode);

    public Mode getMode() {
        return this.mode;
    }

    public Rule getRule() {
        return this.rule;
    }

      // Enum representing the different locations in the game model
    public enum CardDeck implements Location {
        DECK, DISCARD
    }

    // Private constructor to enforce singleton pattern
    private GameModel() {
    }

    // Get the singleton instance of the GameModel
    public static GameModel getInstance() {
        return INSTANCE;
    }


    // Add a listener to the game model
    public void addListener(GameModelListener listener) {
        listenerList.add(listener);
    }

    // Notify all listeners that the game state has changed
    private void notifyListener() {
        List<GameModelListener> listeners = new ArrayList<>(listenerList); // Create a copy of the listenerList
        for (GameModelListener listener : listeners) {
            listener.gameStateChanged();
        }
    }


    // Get a Move object for drawing a card from the deck
    public Move getDeckMove() {
        return new DeckMove(getInstance());
    }

    // Reset the game model to its initial state
    public void reset() {
        setLose(false);
        setHasWon(false);
        deck.reset();
        waste = new Stack<>();
        discoveredCardManager = new DiscoveredCardManager();
        workingStackManager = new WorkingStackManager(deck);
        suitStackManager = new SuitStackManager();
        requireReset = true;
        ScoreView.score.setScore(rule.getStartScore());
        notifyListener();
        requireReset = false;
    }

    public void changeMode(Mode mode) {
        this.mode = mode;
        this.rule = new Rule(mode);
        reset();
    }


    // Discard a card from the deck to the waste pile
    public boolean discard() {
        if (this.deck.isEmpty()) {
            Card c = this.deck.draw();
            waste.add(c);
            notifyListener();
            return true;
        }
        else deck.transferAll(waste);
        ScoreView.score.setScore(ScoreView.score.getScore()- rule.getResetWaste());
        notifyListener();
        return true;
    }

    // Peek at the top card of the waste pile
    public Card peekWaste() {
        if (waste.isEmpty()) {
            return null;
        }
        return waste.peek();
    }

    // Peek at the card on top of a suit stack
    public Card peekSuitStack(SuitStack index) {
        return suitStackManager.viewSuitStack(index);
    }

    // Check if it is possible to draw a card from a specific location
    public boolean canDraw(Location location) {
        if (location instanceof Workingstack) {
            if (workingStackManager.canDraw((Workingstack) location)) {
                return true;
            }
        }

        if (location instanceof SuitStack) {
            if (suitStackManager.canDraw(location)) {
                return true;
            }
        }

        if (location.equals((CardDeck.DECK))) {
            if (deck.isEmpty()) {
                return true;
            }
        }

        if (location.equals((CardDeck.DISCARD))) {
            return !waste.isEmpty();
        }
        return false;
    }

    // Get an array of cards in a specific working stack
    public Card[] getStack(Workingstack index) {
        Card[] cards = new Card[workingStackManager.getWorkingStack(index).size()];
        for (int i = 0; i < workingStackManager.getWorkingStack(index).size(); i++) {
            cards[i] = workingStackManager.getWorkingStack(index).get(i);
        }
        return cards;
    }

    // Move a card from one location to another
    public boolean move(Location source, Location destination, Card card) {
        if (canDraw(source) && canAdd(card, destination)) {
            workingStackManager.addMultiple(workingStackManager.drawMultiple(card, (Workingstack) source),
                    (Workingstack) destination);
            notifyListener();
            return true;
        }
        return false;
    }

    // Move a card from one location to another without specifying the card
    public boolean move(Location source, Location destination) {
        if (source instanceof Workingstack && destination instanceof SuitStack) {
            if (canDraw(source) && suitStackManager.canAdd(workingStackManager.getCards((Workingstack) source).peek())) {
                Card card = workingStackManager.draw((Workingstack) source);
                suitStackManager.add(card);
                //add 10 scores
                ScoreView.score.setScore(ScoreView.score.getScore()+rule.getWorkingStackToSuitStack());
                System.out.println("working to suit");
                notifyListener();
                return true;
            }
        }

        if (source.equals(CardDeck.DISCARD) && destination instanceof SuitStack) {
            if (canDraw(source) && canAdd(waste.peek(), destination)) {
                suitStackManager.add(waste.pop());
                ScoreView.score.setScore(ScoreView.score.getScore()+rule.getWasteToSuitStack());
                notifyListener();
                return true;
            }
        }

        if (source.equals(CardDeck.DISCARD) && destination instanceof Workingstack) {
            workingStackManager.add(waste.pop(), (Workingstack) destination);
            ScoreView.score.setScore(ScoreView.score.getScore()+rule.getWasteToWorkingStack());
            System.out.println("waste to suit");
            notifyListener();
            return true;
        }

        if (source instanceof Workingstack && destination instanceof Workingstack) {
            Card card = workingStackManager.draw((Workingstack) source);
            if (card != null) {
                workingStackManager.add(card, (Workingstack) destination);
                notifyListener();
            }
        }

        if (source instanceof SuitStack && destination instanceof Workingstack) {
            workingStackManager.add(suitStackManager.draw((SuitStack)source), (Workingstack) destination);
            ScoreView.score.setScore(ScoreView.score.getScore()-rule.getSuitStackToWorkingStack());
            notifyListener();
            return true;
        }
        return false;
    }


    // Get a Move object for moving a specific card to a destination
    public Move getCardMove(Card top, Location destination) {
        if (top.equals(peekWaste())) {
            return new OneCardMove(CardDeck.DISCARD, destination, getInstance());
        }

        for (Workingstack ws : Workingstack.values()) {
            if (
                    !workingStackManager.getWorkingStack(ws).isEmpty()&&
                    workingStackManager.getCards(ws).peek().equals(top)
            ) {
                return new OneCardMove(ws, destination, getInstance());
            }
            for (Card c : workingStackManager.getCards(ws)) {
                if (c.equals(top)) {
                    return new MultipleCardsMove(ws, destination, c, getInstance());
                }
            }
        }

        for (SuitStack ss : SuitStack.values()){
            Card card = suitStackManager.viewSuitStack(ss);
            if (!(card==null) &&
            card.equals(top)){
                return new OneCardMove(ss, destination,getInstance());
            }
        }
        return null;
    }

    // Check if it is possible to add a card to a specific destination
    public boolean canAdd(Card top, Location destination) {
        if (destination instanceof Workingstack) {
            if (workingStackManager.canAdd(top, (Workingstack) destination)) {
                return true;
            }
        }

        if (destination instanceof SuitStack) {
            return suitStackManager.canAdd(top);
        }
        return false;
    }

    public boolean checkUndiscovered(Card card) {
        return discoveredCardManager.checkUndiscovered(card);
    }

    public void markUndiscovered(Card card) {
        discoveredCardManager.markUndiscovered(card);
    }

    public void markDiscovered(Card card) {
        Boolean reveal = discoveredCardManager.markDiscovered(card);

        if (reveal ) {
            ScoreView.score.setScore(ScoreView.score.getScore()+rule.getTurnOverWorkingStackCard());
        }
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public boolean hasWon() {
        return hasWon;
    }

    public void setLose(boolean lose) {this.lose = lose;}

    public boolean lose() {return lose;}

    public boolean isRequireReset() {
        return requireReset;
    }

}
