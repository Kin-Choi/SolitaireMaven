package application;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import model.GameModel;

public class WinView extends VBox {

    private static String message;
    private static final int FONT_SIZE = 36;
    private static final int IMAGE_FONT_SIZE = 10;
    private static final String BUTTON_STYLE_NORMAL = " -fx-background-color : transparent; -fx-padding: 5,5,5,5;";
    private static final String BUTTON_STYLE_MOUSEDOWN = " -fx-background-color : transparent; -fx-padding: 5,5,5,5;";


    public WinView(double stageWidth, double stageHeight) {
        // Create the label with the "YOU WIN!" message
        message = "YOU WIN !!\n\n Your Score: "+String.valueOf(ScoreView.score.getScore());
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        messageLabel.setAlignment(Pos.CENTER);


        // Create a button with a reset image
        Button button = new Button();
        Canvas resetImage = createResetImage();
        button.setGraphic(resetImage);
        button.setStyle(BUTTON_STYLE_NORMAL);

        // Set the preferred size of the FinishView to match the stage size
        setPrefSize(stageWidth, stageHeight);

        // Center the label within the FinishView
        setAlignment(Pos.CENTER);

        // Set the styles for the FinishView
        setStyle("-fx-background-color: rgba(246, 247, 50, 1); -fx-background-radius: 5;");

        // Add the label and button to the VBox
        getChildren().addAll(messageLabel, button);

        // Event handler for mouse press on the button
        button.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((Button) event.getSource()).setStyle(BUTTON_STYLE_MOUSEDOWN);
            }
        });

        // Event handler for mouse release on the button
        button.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((Button) event.getSource()).setStyle(BUTTON_STYLE_NORMAL);
                GameModel.getInstance().reset();
            }
        });
    }

    private Canvas createResetImage() {
        double width = 60;
        double height = 90;
        Canvas canvas = new Canvas(width, height);
        GraphicsContext context = canvas.getGraphicsContext2D();

        // Draw text and oval shape on the canvas for the new game image
        context.setTextAlign(TextAlignment.CENTER);
        context.setFill(Color.BLACK);
        context.setFont(Font.font(Font.getDefault().getName(), IMAGE_FONT_SIZE));
        context.fillText("Start Again?", Math.round(width / 2), IMAGE_FONT_SIZE);
        context.setStroke(Color.DARKGREEN);
        context.setLineWidth(10);
        context.strokeOval(width / 4, height / 2 - width / 4 + IMAGE_FONT_SIZE, width / 2, width / 2);

        return canvas;
    }
}
