package univ.orleans.ttl.isokachallenge.orm.entity;

import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class User {

    private final String _username;
    private final LocalDateTime _date;

    public User(String username, LocalDateTime date) {
        this._username = username;
        this._date = date;
    }

    public String getUsername() {
        return this._username;
    }

    public String getDate() {
        return this._date.toString();
    }

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    @Override
    public String toString() {
        return "USER: " + this._username + " (" + this._date + ")";
    }
}
