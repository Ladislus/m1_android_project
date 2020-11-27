package univ.orleans.ttl.isokachallenge.orm.entity;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Représentation d'un compte utilisateur
 */
public class User {

    private final String _username;
    private final LocalDateTime _date;
    private final String _salt;

    /**
     * Constructeur par défaut
     * @param username Le nom d'utilisateur
     * @param date La date d'inscription
     * @param salt Le sel utilisé pour hasher le mot de passe
     */
    public User(@NonNull String username, @NonNull LocalDateTime date, @NonNull String salt) {
        this._username = username;
        this._date = date;
        this._salt = salt;
    }

    /**
     * Constructeur pour créer un nouvel utilisateur
     * Ce constructeur génère le salt de l'utilisateur
     * @param username Le nom d'utilisateur
     * @param date La date d'inscription
     */
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

    /**
     * Getter de la date d'inscription de l'utilisateur, au format "jour/mois/année, heure'minute'seconde"
     * Exemple: 29/10/2020, 19'20'00
     * @return La date d'inscription de l'utilisateur
     */
    public String getFormattedDate() {
        return this._date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH'h'mm"));
    }

    public String getSalt() {
        return this._salt;
    }

    /**
     * Fonction de hashage de mot de passe avec BCrypt
     * @param password Le mot de passe en clair
     * @param salt Le salt à utiliser
     * @return Le mot de passe hashé avec BCrypt
     */
    public static String hash(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    /**
     * Method de transformation de l'instance d
     * @param password Le mot de pa
     * @return L'utilisateur au format JSONObject
     */
    public JSONObject toJson(String password) {
        try {
            return new JSONObject()
                    .put("username", this._username)
                    .put("salt", this._salt)
                    .put("password", User.hash(password, this._salt))
                    .put("date", this._date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "USER: " + this._username + " (" + this._date + ", " + this._salt + ")";
    }
}
