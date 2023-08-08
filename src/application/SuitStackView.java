package application;

import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import model.GameModel;
import model.GameModelListener;
import model.SuitStackManager.SuitStack;
import cards.Card;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class SuitStackView extends StackPane implements GameModelListener {

    // Constants for padding and border style
    private static final int PADDING = 5;
    private static final String BORDER_STYLE = "-fx-border-color : lightgray;" + "-fx-border-width:1;" + "-fx-border-radius: 5.0;";

    private final SuitStack index;          // The suit stack index
    private final CardDragHandler dragHandler;  // Drag handler for card dragging

    public SuitStackView(SuitStack index) {
        this.index = index;

        // Set padding and border style for the StackPane
        setPadding(new Insets(PADDING));
        setStyle(BORDER_STYLE);

        final ImageView image = new ImageView(CardImage.getBack());
        image.setFitWidth(60);
        image.setFitHeight(90);
        image.setVisible(false);
        getChildren().add(image);

        dragHandler = new CardDragHandler(image);
        image.setOnDragDetected(dragHandler);
        setOnDragOver(createOnDragOverHandler());
        setOnDragDropped(createOnDragDroppedHandler());
        image.setCursor(Cursor.HAND);

        GameModel.getInstance().addListener(this);
    }

    private EventHandler<DragEvent> createOnDragOverHandler() {
        return event -> {
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        };
    }

    private EventHandler<DragEvent> createOnDragDroppedHandler() {
        return event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                GameModel.getInstance().getCardMove(Card.get(db.getString()), index).move();
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        };
    }

    @Override
    public void gameStateChanged() {
        if (!GameModel.getInstance().canDraw(index)) {
            getChildren().get(0).setVisible(false);
        } else {
            getChildren().get(0).setVisible(true);
            Card topCard = GameModel.getInstance().peekSuitStack(index);
            ImageView image = (ImageView) getChildren().get(0);
            image.setImage(CardImage.getImage(topCard));
            dragHandler.setCard(topCard);
            image.setOnMouseClicked(createDoubleClickHandler());

        }
    }

    private EventHandler<MouseEvent> createDoubleClickHandler() {
        return mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    GameModel.getInstance().autoMove(index);
                }
            }
        };
    }

}
