package application;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.GameModel;
import model.GameModelListener;
import model.GameModel.CardDeck;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import cards.Card;
import javafx.geometry.Insets;

public class WasteView extends HBox implements GameModelListener {

    private final CardDragHandler dragHandler;

    public WasteView() {
        // Set padding for the HBox
        setPadding(new Insets(5));
        ImageView view;
        // Create an ImageView with the card back image and make it initially invisible
        view = new ImageView(CardImage.getBack());
        
        view.setFitWidth(60);
        view.setFitHeight(90);
        view.setVisible(false);
        view.setCursor(Cursor.HAND);

        // Create a CardDragHandler and set it as the drag handler for the ImageView
        dragHandler = new CardDragHandler(view);
        view.setOnDragDetected(dragHandler);

        // Add the ImageView to the HBox
        getChildren().add(view);

        // Register the WasteView as a listener to the GameModel
        GameModel.getInstance().addListener(this);
    }

    @Override
    public void gameStateChanged() {
        if (!GameModel.getInstance().canDraw(CardDeck.DISCARD)) {
            // If the waste pile cannot be drawn from, hide the ImageView
            getChildren().get(0).setVisible(false);
        } else {
            // If the waste pile can be drawn from, show the ImageView and update the displayed card
            getChildren().get(0).setVisible(true);
            Card topCard = GameModel.getInstance().peekWaste();
            ImageView image = (ImageView) this.getChildren().get(0);
            image.setImage(CardImage.getImage(topCard));
            dragHandler.setCard(topCard);
            image.setOnMouseClicked(createDoubleClickHandler());
        }
    }


    private EventHandler<MouseEvent> createDoubleClickHandler() {
        return mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    GameModel.getInstance().autoMove(CardDeck.DISCARD);
                }
            }
        };
    }

}
