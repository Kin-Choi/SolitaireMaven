package move;

import cards.Card;
import model.GameModel;
import model.Location;

public class MultipleCardsMove implements Move {

    private Location source;
    private Location destination;
    private Card card;
    GameModel gameModel;

    // Constructor to initialize the MultipleCardsMove with source, destination, card, and GameModel
    public MultipleCardsMove(Location source, Location destination, Card card, GameModel gameModel) {
        this.source = source;
        this.destination = destination;
        this.card = card;
        this.gameModel = gameModel;
    }

    // Implementation of the move() method from the Move interface
    @Override
    public boolean move() {
        // Call the move() method of the GameModel to perform the move with multiple cards
        boolean success = gameModel.move(source, destination, card);
        return success;
    }
}
