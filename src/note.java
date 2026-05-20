public class note {
    String text = "";
    String password = "";
    boolean locked = false;

    void setText(String t) {
        if (!locked) text = t;
    }

    String getText() {
        if (locked) return "🔒 Locked Note";
        return text;
    }

    void lock(String pass) {
        password = pass;
        locked = true;
    }

    boolean unlock(String pass) {
        if (password.equals(pass)) {
            locked = false;
            return true;
        }
        return false;
    }

    boolean isLocked() {
        return locked;
    }
}