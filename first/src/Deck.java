import java.util.*;

public class Deck {

    ArrayList<card> cards = new ArrayList<>();

    public Deck() {

        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

        for (String s : suits) {
            for (int i = 1; i <= 13; i++) {
                cards.add(new card(s, i));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public card drawCard() {
        return cards.remove(0);
    }
}