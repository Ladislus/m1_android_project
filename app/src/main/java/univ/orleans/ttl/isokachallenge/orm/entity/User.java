package univ.orleans.ttl.isokachallenge.orm.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Représentation d'un compte utilisateur
 */
public class User {

    @NonNull
    private final String _username;
    @NonNull
    private final LocalDateTime _date;
    @NonNull
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
    public User(@NonNull String username, @NonNull LocalDateTime date) {
        this(username, date, BCrypt.gensalt(12));
    }

    /**
     * Fonction pour parser un JSONObject vers un User
     * @param json L'utilisateur au format JSONObject
     * @return L'utilisateur correspondant
     * @throws JSONException Si le JSONObject ne contient pas tous les champs necessaire
     */
    @NonNull
    public static User fromJson(@NonNull JSONObject json) throws JSONException {
        return new User(
                json.getString("username"),
                LocalDateTime.parse(json.getString("date")),
                json.getString("salt")
        );
    }

    @NonNull
    public String getUsername() {
        return this._username;
    }

    @NonNull
    public String getDate() {
        return this._date.toString();
    }

    /**
     * Getter de la date d'inscription de l'utilisateur, au format "jour/mois/année, heure'minute'seconde"
     * Exemple: 29/10/2020, 19'20'00
     * @return La date d'inscription de l'utilisateur
     */
    @NonNull
    public String getFormattedDate() {
        return this._date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH'h'mm"));
    }

    @NonNull
    public String getSalt() {
        return this._salt;
    }

    /**
     * Fonction de hashage de mot de passe avec BCrypt
     * @param password Le mot de passe en clair
     * @param salt Le salt à utiliser
     * @return Le mot de passe hashé avec BCrypt
     */
    @NonNull
    public static String hash(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    /**
     * Obtention de la représentation d'un utilisateur au format JSON (pour l'envoi dans des requêtes)
     * @param password Le mot de passe de l'utilisateur (necessaire pour les requêtes "save")
     * @return L'utilisateur au format JSONObject et son mot de passe
     */
    @Nullable
    public JSONObject toJson(@NonNull String password) {
        try {
            return new JSONObject()
                    .put("username", this._username)
                    .put("salt", this._salt)
                    .put("password", User.hash(password, this._salt))
                    // Formatage pour faciliter le parsing de ma date par le serveur Flask
                    .put("date", this._date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        } catch (JSONException e) {
            // Error lors du parsing de vers JSON
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "USER: " + this._username + " (" + this._date + ", " + this._salt + ")";
    }
}
