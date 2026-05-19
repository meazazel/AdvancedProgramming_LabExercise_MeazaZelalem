import java.util.*;

public class Evaluater {

    public static Result evaluate(ArrayList<card> cards) {

        HashMap<Integer, Integer> count = new HashMap<>();
        ArrayList<Integer> values = new ArrayList<>();

        for (card c : cards) {
            int v = c.getValue();
            values.add(v);
            count.put(v, count.getOrDefault(v, 0) + 1);
        }

        Collections.sort(values, Collections.reverseOrder());

        int pair = -1;
        int three = -1;
        int four = -1;

        for (int v : count.keySet()) {
            int freq = count.get(v);

            if (freq == 4) four = v;
            else if (freq == 3) three = v;
            else if (freq == 2) pair = v;
        }

        if (four != -1) return new Result(7, values);
        if (three != -1 && pair != -1) return new Result(6, values);
        if (three != -1) return new Result(3, values);
        if (pair != -1) return new Result(1, values);

        return new Result(0, values);
    }
}