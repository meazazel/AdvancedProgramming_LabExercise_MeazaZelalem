import java.util.*;

public class Player {

    String name;
    ArrayList<card> hand = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void addCard(card c) {
        hand.add(c);
    }

    public ArrayList<card> getHand() {
        return hand;
    }
}