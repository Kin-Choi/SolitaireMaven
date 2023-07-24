package application;

import cards.Card;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;


/**
 * Stores a string representing the card dragged.
 */
public class CardDragHandler implements EventHandler<MouseEvent> {
    // ClipboardContent object used to store the data being dragged
    private static final ClipboardContent CLIPBOARD_CONTENT = new ClipboardContent();

    // Instance variables
    private Card card;              // Reference to the card being dragged
    private ImageView imageView;    // Reference to the ImageView representing the card

    
    // Constructor
    CardDragHandler(ImageView imageView) {
        this.imageView = imageView;
    }

    // Setter for the card
    void setCard(Card card) {
        this.card = card;
    }
    

    // Event handling method for drag events
    @Override
    public void handle(MouseEvent pMouseEvent) {
        // Start the drag-and-drop operation on the ImageView
        Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
        
        //Set drag image 
        Image image = CardImage.getImage(card); 
        db.setDragViewOffsetX(0);
        db.setDragView(image);
        double offsetX = image.getWidth() / 2;
        double offsetY = image.getHeight()/  2;
        db.setDragViewOffsetX(offsetX);
        db.setDragViewOffsetY(offsetY);

     
        // Put the ID string of the card into the ClipboardContent
        CLIPBOARD_CONTENT.putString(card.getIDString());

        // Set the content of the Dragboard to the ClipboardContent
        db.setContent(CLIPBOARD_CONTENT);
        
        // Consume the event to prevent further handling
        pMouseEvent.consume();
        
        
    }
}
