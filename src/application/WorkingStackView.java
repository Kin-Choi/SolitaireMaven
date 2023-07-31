package application;

import cards.Card;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.GameModel;
import model.GameModelListener;
import model.WorkingStackManager.Workingstack;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;


public class WorkingStackView extends StackPane implements GameModelListener {

    // Constants for padding and vertical offset between cards in the stack
    private static final int PADDING = 5;
    private static final int Y_OFFSET = 17;

    private Workingstack index;     // The working stack index

    public WorkingStackView(Workingstack index) {
        this.index = index;

        // Set padding for the StackPane
        setPadding(new Insets(PADDING));

        // Build the initial layout of the working stack
        buildLayout();

        // Register the WorkingStackView as a listener to the GameModel
        GameModel.getInstance().addListener(this);
    }

    private void buildLayout() {
        getChildren().clear();
        int offset = 0;
        Card[] stack = GameModel.getInstance().getStack(index);

        // Create ImageView objects for each card in the stack and add them to the StackPane
        for (int i = 0; i <stack.length ; i++) {
        	Card card = stack[i];
        	Boolean dragAllowed = false;
        	final ImageView image;
        	if (GameModel.getInstance().checkUndiscovered(card)) {
        		image = new ImageView(CardImage.getBack());
        		dragAllowed = false;
        	} else {
        		image = new ImageView(CardImage.getImage(card));
        		dragAllowed = true;
        	}
            image.setFitHeight(90);
            image.setFitWidth(60);
            image.setTranslateY(Y_OFFSET * offset);
            offset++;
            getChildren().add(image);

            // Set event handlers for drag detection, drag over, and drag dropped for each card
            setOnDragOver(createDragOverHandler());
            setOnDragDropped(createDragDroppedHandler());

            if (dragAllowed) {
            	image.setOnDragDetected(createDragDetectedHandler(image, card, stack, i ));
            	image.setCursor(Cursor.HAND);
                image.setOnMouseClicked(createDoubleClickHandler(card));

            }          
        }
    }

    private EventHandler<MouseEvent> createDragDetectedHandler(final ImageView imageView, final Card card, final Card[] stack, int i) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(card.getIDString());
                db.setContent(content);
                
                //Set drag image 
                StackPane root = new StackPane();
                int offset = 0;
                for (int j=i; j < stack.length ; j++) {          	
                	ImageView imageViewj = new ImageView(CardImage.getImage(stack[j]));
                	imageViewj.setTranslateY(Y_OFFSET * offset);
                    offset++;
                	root.getChildren().add(imageViewj);          	
                }

                // Create a WritableImage and capture the StackPane's snapshot
                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT); // Make the background transparent
                WritableImage combinedImage = root.snapshot(parameters, null);
                Image image = imageView.getImage(); 
                db.setDragViewOffsetX(0);
                db.setDragView(combinedImage);
                double offsetX = image.getWidth() / 2;
                double offsetY = image.getHeight()/  2;
                db.setDragViewOffsetX(offsetX);
                db.setDragViewOffsetY(offsetY);
                
                event.consume();
            }
        };
    }

    private EventHandler<DragEvent> createDragOverHandler() {
        return new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (GameModel.getInstance().canAdd(Card.get(event.getDragboard().getString()), index)) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        };
    }

    private EventHandler<DragEvent> createDragDroppedHandler() {
        return new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    GameModel.getInstance().getCardMove(Card.get(db.getString()), index).move();
                    success = true;
                }
                event.setDropCompleted(success);
            }
        };
    }

    private EventHandler<MouseEvent> createDoubleClickHandler(Card card) {
        return (MouseEvent mouseEvent) -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    GameModel.getInstance().autoMove(card, index);
//                                   GameModel.getInstance().autoMove( index);


                }
            }
        };
    }


    @Override
    public void gameStateChanged() {
        buildLayout();
    }
}
