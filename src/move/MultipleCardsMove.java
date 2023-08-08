package move;

import cards.Card;
import model.GameModel;
import model.Location;

public class MultipleCardsMove implements Move {

    private final Location source;
    private final Location destination;
    private final Card card;
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
    public void move() {
        // Call the move() method of the GameModel to perform the move with multiple cards
        gameModel.move(source, destination, card);
    }
}
