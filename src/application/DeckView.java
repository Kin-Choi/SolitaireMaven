package application;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.GameModel;
import model.GameModel.CardDeck;
import model.GameModelListener;
import javafx.scene.image.ImageView;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;

public class DeckView extends HBox implements GameModelListener {

    // Button styles for normal and mouse down states
    private static final String BUTTON_STYLE_NORMAL = " -fx-background-color : transparent; -fx-padding: 5,5,5,5;";
    private static final String BUTTON_STYLE_MOUSEDOWN = " -fx-background-color : transparent; -fx-padding: 5,5,5,5;";
    private static final int IMAGE_FONT_SIZE = 10;

    public DeckView() {
        // Reset the game model
        GameModel.getInstance().reset();

        // Create a button with a deck image
        Button button = new Button();
        ImageView deck = new ImageView(CardImage.getBack());
        deck.setFitWidth(60);
        deck.setFitHeight(90);
        button.setGraphic(deck);
        button.setStyle(BUTTON_STYLE_NORMAL);
        button.setCursor(Cursor.HAND);

        // Event handler for mouse press on the button
        button.setOnMousePressed(event -> ((Button) event.getSource()).setStyle(BUTTON_STYLE_MOUSEDOWN));

        // Event handler for mouse release on the button
        button.setOnMouseReleased(event -> {
            ((Button) event.getSource()).setStyle(BUTTON_STYLE_NORMAL);
            if (!GameModel.getInstance().canDraw(CardDeck.DECK)) {
//                    GameModel.getInstance().reset();
                GameModel.getInstance().getDeckMove().move();

            } else {
                GameModel.getInstance().getDeckMove().move();
            }
        });

        // Add the button to the HBox
        getChildren().add(button);

        // Register the DeckView as a listener to the GameModel
        GameModel.getInstance().addListener(this);
    }

    @Override
    public void gameStateChanged() {
        if (!GameModel.getInstance().canDraw(CardDeck.DECK)) {
            // Create a new game image if the deck cannot be drawn
            ((Button) getChildren().get(0)).setGraphic(createNewGameImage());
        } else {
            // Set the deck image if the deck can be drawn
            ImageView view = new ImageView(CardImage.getBack());
            view.setFitWidth(60);
            view.setFitHeight(90);
            ((Button) getChildren().get(0)).setGraphic(view);
        }
    }

    private Canvas createNewGameImage() {
        double width = 60;
        double height = 90;
        Canvas canvas = new Canvas(width, height);
        GraphicsContext context = canvas.getGraphicsContext2D();

        // Draw text and oval shape on the canvas for the new game image
        context.setTextAlign(TextAlignment.CENTER);
        context.setFill(Color.DARKKHAKI);
        context.setFont(Font.font(Font.getDefault().getName(), IMAGE_FONT_SIZE));
//        context.fillText("Start Again?", Math.round(width / 2), IMAGE_FONT_SIZE);
        context.setStroke(Color.DARKGREEN);
        context.setLineWidth(10);
        context.strokeOval(width / 4, height / 2 - width / 4 + IMAGE_FONT_SIZE, width / 2, width / 2);

        return canvas;
    }
}
