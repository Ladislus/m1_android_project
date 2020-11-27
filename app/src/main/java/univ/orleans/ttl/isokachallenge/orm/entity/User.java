package univ.orleans.ttl.isokachallenge.orm.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class User {

    private final String _username;
    private final LocalDateTime _date;
    private final String _salt;

    public User(String username, LocalDateTime date, String salt) {
        this._username = username;
        this._date = date;
        this._salt = salt;
    }


    public User(String username, LocalDateTime date) {
        this(username, date, BCrypt.gensalt(12));
    }

    public static User fromJson(JSONObject json) throws JSONException {
        return new User(
                json.getString("username"),
                LocalDateTime.parse(json.getString("date")),
                json.getString("salt")
        );
    }

    public String getUsername() {
        return this._username;
    }

    public String getDate() {
        return this._date.toString();
    }

    public String getFormattedDate() {
        return this._date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH'h'mm"));
    }

    public String getSalt() {
        return this._salt;
    }

    public static String hash(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    public JSONObject toJson(String password) {
        try {
            return new JSONObject()
                    .put("username", this._username)
                    .put("salt", this._salt)
                    .put("password", User.hash(password, this._salt))
                    .put("date", this._date.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "USER: " + this._username + " (" + this._date + ")";
    }
}
