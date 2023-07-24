package application;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.GameModel;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class ResetView extends HBox {

    private static final String BUTTON_STYLE_NORMAL = " -fx-background-color : transparent; -fx-padding: 5,5,5,5;";
    private static final String BUTTON_STYLE_MOUSEDOWN = " -fx-background-color : transparent; -fx-padding: 5,5,5,5;";
    private static final int IMAGE_FONT_SIZE = 10;

    public ResetView() {

        // Create a button with a reset image
        Button button = new Button();
        Canvas resetImage = createResetImage();
        button.setGraphic(resetImage);
        button.setStyle(BUTTON_STYLE_NORMAL);
        button.setCursor(Cursor.HAND);

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

        // Add the button to the HBox
        getChildren().add(button);

        // Set padding to add space on the left side of the HBox
        setPadding(new Insets(0, 0, 0, 50)); // Top, Right, Bottom, Left padding
    }

    private Canvas createResetImage() {
        double width = 60;
        double height = 90;
        Canvas canvas = new Canvas(width, height);
        GraphicsContext context = canvas.getGraphicsContext2D();

        // Draw text and oval shape on the canvas for the new game image
        context.setTextAlign(TextAlignment.CENTER);
        context.setFill(Color.DARKKHAKI);
        context.setFont(Font.font(Font.getDefault().getName(), IMAGE_FONT_SIZE));
        context.fillText("Start Again?", Math.round(width / 2), IMAGE_FONT_SIZE);
        context.setStroke(Color.DARKGREEN);
        context.setLineWidth(10);
        context.strokeOval(width / 4, height / 2 - width / 4 + IMAGE_FONT_SIZE, width / 2, width / 2);

        return canvas;
    }
}
