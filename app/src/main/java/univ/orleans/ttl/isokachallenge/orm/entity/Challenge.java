package univ.orleans.ttl.isokachallenge.orm.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Représentation d'un challenge
 */
public class Challenge {

    @Nullable
    private final Integer _id;
    @NonNull
    private final String _name;
    @NonNull
    private final Boolean _type;
    @NonNull
    private final String _theme;
    @NonNull
    private final LocalDateTime _date;
    @NonNull
    private final Integer _timer;
    @NonNull
    private final String _desc;

    /**
    * Constructeur par défaut
    * Ce constructeur est utilisé lors de la reception
    * d'un challenge depuis la base de données distante
     * @param id L'ID du challenge
     * @param name Le nom du challenge
     * @param type Le type de challenge (true = le meilleur gagne, false = le pire gagne)
     * @param theme Le lien vers l'image de thème (l'image à recopier)
     * @param date La date de fin du challenge (Date limite de participation)
     * @param timer Le temps disponible pour reproduire le dessin (en secondes)
     * @param desc La description du challenge
     */
    public Challenge(@Nullable Integer id, @NonNull String name, @NonNull Boolean type, @NonNull String theme, @NonNull LocalDateTime date, @NonNull Integer timer, @NonNull String desc) {
        this._id = id;
        this._name = name;
        this._type = type;
        this._theme = theme;
        this._date = date;
        this._timer = timer;
        this._desc = desc;
    }

    /**
     * Constructeur utilisé pour créer un nouveau challenge
     * Un challenge qui n'est présente que en locale ne peut pas encore avoir d'ID,
     * car c'est l'auto-increment de la base de données distante qui attribut les IDs
     * Utiliser le constructeur par défaut et donner manuellement un ID à un dessin
     * Peut entrainer de gros problèmes
     * @param name Le nom du challenge
     * @param type Le type de challenge (true = le meilleur gagne, false = le pire gagne)
     * @param theme Le lien vers l'image de thème (l'image à recopier)
     * @param date La date de fin du challenge (Date limite de participation)
     * @param timer Le temps disponible pour reproduire le dessin (en secondes)
     * @param desc La description du challenge
     */
    public Challenge(@NonNull String name, @NonNull Boolean type, @NonNull String theme, @NonNull LocalDateTime date, @NonNull Integer timer, @NonNull String desc) {
        this(null, name, type, theme, date, timer, desc);
    }

    /**
     * Fonction pour parser un JSONObject vers un Challenge
     * @param json Le challenge au format JSONObject
     * @return Le challenge correspondant
     * @throws JSONException Si le JSONObject ne contient pas tous les champs necessaires
     */
    @NonNull
    public static Challenge fromJson(JSONObject json) throws JSONException {
        return new Challenge(
                json.getInt("id"),
                json.getString("name"),
                json.getBoolean("type"),
                json.getString("theme"),
                LocalDateTime.parse(json.getString("date")),
                json.getInt("timer"),
                json.getString("desc")
        );
    }

    @Nullable
    public Integer getId() {
        return this._id;
    }

    @NonNull
    public String getName() {
        return this._name;
    }

    @NonNull
    public Boolean getType() {
        return this._type;
    }

    @NonNull
    public String getTheme() {
        return this._theme;
    }

    @NonNull
    public String getDate() {
        return this._date.toString();
    }

    /**
     * Getter de la date création du challenge, au format "jour/mois/année, heure'minute'seconde"
     * Exemple: 29/10/2020, 19'20'00
     * @return La date de création du challenge vers Imgur
     */
    @NonNull
    public String getFormattedDate() {
        return this._date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH'h'mm"));
    }

    @NonNull
    public Integer getTimer() {
        return this._timer;
    }

    @NonNull
    public String getDesc() { return this._desc; }

    /**
     * Obtention de la représentation d'un challenge au format JSON (pour l'envoi dans des requêtes)
     * @return Le challenge au format JSONObject (Retourne null si l'opération s'est mal déroulée)
     */
    @Nullable
    public JSONObject toJson() {
        try {
            return new JSONObject()
                    .put("id", this._id)
                    .put("name", this._name)
                    .put("type", this._type)
                    .put("theme", this._theme)
                    .put("desc", this._desc)
                    // Formatage pour faciliter le parsing de ma date par le serveur Flask
                    .put("date", this._date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .put("timer", this._timer);
        } catch (JSONException e) {
            // Error lors du parsing de vers JSON
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "CHALLENGE: " + this._id + " (" + this._name + ", " + this._type + this._theme + ", " + this._date + ", " + this._timer + ", " + this._desc + ")";
    }
}
