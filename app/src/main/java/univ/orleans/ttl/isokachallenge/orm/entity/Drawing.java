package univ.orleans.ttl.isokachallenge.orm.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Représentation d'un dessin
 */
public class Drawing {

    @Nullable
    private final Integer _id;
    @NonNull
    private final String _link;
    @NonNull
    private final LocalDateTime _date;

    /**
     * Constructeur par défaut
     * @param id L'id du dessin
     * @param link Le lien Imgur de l'image
     * @param date La date de publication
     */
    public Drawing(@Nullable Integer id, @NonNull String link, @NonNull LocalDateTime date) {
        this._id = id;
        this._link = link;
        this._date = date;
    }

    /**
     * Constructeur utilisé pour créer un nouvel utilisateur
     * Un image qui n'est présente que en locale ne peut pas encore avoir d'ID,
     * car c'est l'auto-increment de la base de données distante qui attribut les IDs
     * Utiliser le constructeur par défaut et donner manuellement un ID à un dessin
     * Peut entrainer de gros problèmes
     * @param link Le lien Imgur vers l'image
     * @param date La date de publication
     */
    public Drawing(@NonNull String link, @NonNull LocalDateTime date) {
        this(null, link, date);
    }

    /**
     * Fonction pour parser un JSONObject vers un Drawing
     * @param json Le dessin au format JSONObject
     * @return Le dessin correspondant
     * @throws JSONException Si le JSONObject ne contient pas tous les champs necessaires
     */
    @NonNull
    public static Drawing fromJson(@NonNull JSONObject json) throws JSONException {
        return new Drawing(
                json.getInt("id"),
                json.getString("link"),
                LocalDateTime.parse(json.getString("date"))
        );
    }

    @NonNull
    public Integer getId() {
        return this._id;
    }

    @NonNull
    public String getLink() {
        return this._link;
    }

    @NonNull
    public String getDate() {
        return this._date.toString();
    }

    /**
     * Getter de la date d'envoi du dessin à l'API Imgur, au format "jour/mois/année, heure'minute'seconde"
     * Exemple: 29/10/2020, 19'20'00
     * @return La date de d'envoi du desin vers Imgur
     */
    @NonNull
    public String getFormattedDate() {
        return this._date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH'h'mm"));
    }

    /**
     * Obtention de la représentation d'un dessin au format JSON (pour l'envoi dans des requêtes)
     * @return Le dessin au format JSONObject (Retourne null si l'opération s'est mal déroulée)
     */
    @Nullable
    public JSONObject toJson() {
        try {
            return new JSONObject()
                    .put("id", this._id)
                    .put("link", this._link)
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
        return "DRAWING: " + this._id + " (" + this._link + ", " + this._date + ")";
    }
}
