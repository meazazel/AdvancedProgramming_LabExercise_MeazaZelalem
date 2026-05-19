public class card {
    String suit;
    int value;

    public card(String suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public String getImageName() {

        String v;

        if (value == 1) v = "ace";
        else if (value == 11) v = "jack";
        else if (value == 12) v = "queen";
        else if (value == 13) v = "king";
        else v = String.valueOf(value);

        return v + "_of_" + suit.toLowerCase() + ".png";
    }
}