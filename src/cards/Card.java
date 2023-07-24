package cards;

public class Card {

    // Two-dimensional array to store Card objects for each combination of Suit and Rank
    private static Card[][] cards = new Card[Suit.values().length][Rank.values().length];

    private final Rank rank;   // The rank of the card
    private final Suit suit;   // The suit of the card

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public static Card getCard(Rank rank, Suit suit) {
        // Check if the Card object for the specified rank and suit combination already exists
        if (cards[suit.ordinal()][rank.ordinal()] == null) {
            // If not, create a new Card object and store it in the cards array
            cards[suit.ordinal()][rank.ordinal()] = new Card(rank, suit);
        }
        // Return the Card object from the cards array
        return cards[suit.ordinal()][rank.ordinal()];
    }

    public String getIDString() {
        // Generate a unique ID string for the card based on its rank and suit
        return Integer.toString(getSuit().ordinal() * Rank.values().length + getRank().ordinal());
    }

    public static Card get(String pId) {
        assert pId != null;
        // Parse the ID string and retrieve the corresponding Card object from the cards array
        int id = Integer.parseInt(pId);
        return getCard(Rank.values()[id % Rank.values().length], Suit.values()[id / Rank.values().length]);
    }

    @Override
    public String toString() {
        // Return a string representation of the card in the format "Rank of Suit"
        return rank + " of " + suit;
    }

    public boolean equals(Card card) {
        if (card == null) return false;
        else return (card.getRank() == this.getRank() && card.getSuit() == this.getSuit());
    }

}
