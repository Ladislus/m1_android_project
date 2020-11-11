package univ.orleans.ttl.isokachallenge.orm.entity;

import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class User {

    private String _userame;
    private final LocalDateTime _date;

    public User(String username, LocalDateTime date) {
        this._userame = username;
        this._date = date;
    }

    public String getUsername() {
        return this._userame;
    }

    public String getDate() {
        return this._date.toString();
    }

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}
