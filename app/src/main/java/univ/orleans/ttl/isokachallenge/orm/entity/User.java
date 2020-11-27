package univ.orleans.ttl.isokachallenge.orm.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class User {

    private final String _username;
    private final LocalDateTime _date;

    public User(String username, LocalDateTime date) {
        this._username = username;
        this._date = date;
    }

    public static User fromJson(JSONObject json) throws JSONException {
        return new User(
                json.getString("username"),
                LocalDateTime.parse(json.getString("date"))
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

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public JSONObject toJson(String password) {
        try {
            return new JSONObject()
                    .put("username", this._username)
                    .put("password", User.hash(password))
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
