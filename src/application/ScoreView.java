package application;

import cards.Mode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.GameModel;
import model.GameModelListener;
import result.Score;

public class ScoreView extends HBox implements GameModelListener {
    private static final int FONT_SIZE = 20;
    public static Score score = new Score(Mode.NORMAL);

    public ScoreView() {
        // Reset the game model
        GameModel.getInstance().reset();
        score= new Score(GameModel.getInstance().getMode());
        Text scoreText = new Text("Score:  ");
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        Text scoreLabel = new Text(String.valueOf(score.getScore()));
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        this.setStyle("-fx-padding: 10px;");
        // Add the score to the HBox
        getChildren().add(scoreText);
        getChildren().add(scoreLabel);

        // Register the DeckView as a listener to the GameModel
        GameModel.getInstance().addListener(this);
    }

    @Override
    public void gameStateChanged() {
        Text scoreLabel = (Text) getChildren().get(1);
        scoreLabel.setText(String.valueOf(score.getScore()));
        if (score.getScore()<=GameModel.getInstance().getRule().getLoseScore()) {
            GameModel.getInstance().setLose(true);
        }
    }
}
