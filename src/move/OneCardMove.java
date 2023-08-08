package move;

import model.GameModel;
import model.Location;

public class OneCardMove implements Move {

    private final Location source;
    private final Location destination;
    GameModel gameModel;

    // Constructor to initialize the OneCardMove with source, destination, and GameModel
    public OneCardMove(Location source, Location destination, GameModel gameModel) {
        this.source = source;
        this.destination = destination;
        this.gameModel = gameModel;
    }

    // Implementation of the move() method from the Move interface
    @Override
    public void move() {
        // Call the move() method of the GameModel to perform the move with one card
        gameModel.move(source, destination);
    }
}
