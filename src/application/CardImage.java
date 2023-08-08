package application;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cards.Card;
import javafx.scene.image.Image;

public class CardImage {

    // A map to store card images using their code as the key
    private static final Map<String, Image> cards = new HashMap<>();

    // Location and suffix for card image files
    private static final String IMAGE_LOCATION = "Images/";
    private static final String IMAGE_SUFFIX = ".png";

    // Arrays to map ranks and suits to their corresponding codes
    private static final String[] RANK_CODE = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k"};
    private static final String[] SUIT_CODE = {"c", "d", "s", "h"};

    // Private method to generate a code for a card based on its rank and suit
    private static String getCode(Card card) {
        return RANK_CODE[card.getRank().ordinal()] + SUIT_CODE[card.getSuit().ordinal()];
    }

    // Public method to get the image of a specific card
    public static Image getImage(Card card) {
        assert card != null;
        return getImage(getCode(card));
    }

    // Private method to retrieve the image based on its code
    private static Image getImage(String code) {
        // Check if the image is already stored in the map
        Image image = cards.get(code);

        // If the image is not in the map, load it from the file and add it to the map
        if (image == null) {
            image = new Image(Objects.requireNonNull(CardImage.class.getClassLoader().getResourceAsStream(IMAGE_LOCATION + code + IMAGE_SUFFIX)),70,100,true,false);
            cards.put(code, image);
        }

        // Return the image
        return image;
    }

    // Public method to get the image of the card back
    public static Image getBack() {
        return getImage("back");
    }

}
