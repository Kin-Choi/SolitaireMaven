package move;

import model.GameModel;

public class DeckMove implements Move {
    GameModel gameModel;

    // Constructor to initialize the DeckMove with a GameModel instance
    public DeckMove(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    // Implementation of the move() method from the Move interface
    @Override
    public boolean move() {
        // Call the discard() method of the GameModel to perform the deck move
        return gameModel.discard();
    }
}
