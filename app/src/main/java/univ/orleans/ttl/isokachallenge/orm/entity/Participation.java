package univ.orleans.ttl.isokachallenge.orm.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import univ.orleans.ttl.isokachallenge.orm.DB;

/**
 * Représentation d'une participation à un challenge
 */
public class Participation {

    private final User _user;
    private final Drawing _drawing;
    private final Challenge _challenge;
    private final Boolean _isCreator;
    private final Integer _votes;

    /**
     * Constructeur par défaut
     * @param user L'utilisateur qui a participé
     * @param drawing Le dessin de l'utilisateur
     * @param challenge Le challenge sur lequel l'utilisateur a participé
     * @param isCreator L'utilisateur qui a participé est-t-il le créateur
     * @param votes Le nombre de vote de la participation
     */
    public Participation(@NonNull User user, @NonNull Drawing drawing, @NonNull Challenge challenge, @NonNull Boolean isCreator, @NonNull Integer votes) {
        this._user = user;
        this._drawing = drawing;
        this._challenge = challenge;
        this._isCreator = isCreator;
        this._votes = votes;
    }

    /**
     * Constructeur de participation qui met à 0 le nombre de vote
     * @param user L'utilisateur qui a participé
     * @param drawing Le dessin de l'utilisateur
     * @param challenge Le challenge sur lequel l'utilisateur a participé
     * @param isCreator L'utilisateur qui a participé est-t-il le créateur
     */
    public Participation(@NonNull User user, @NonNull Drawing drawing, @NonNull Challenge challenge, @NonNull Boolean isCreator) {
        this(user, drawing, challenge, isCreator, 0);
    }


    /**
     * Fonction pour parser un JSONObject vers un Participation
     * @param json La participation au format JSONObject
     * @return La participation correspondante
     * @throws JSONException Si le JSONObject ne contient pas tous les champs necessaire
     */
    public static Participation fromJson(@NonNull JSONObject json) throws JSONException {
        return new Participation(
                DB.getInstance().getUser(json.getString("user")),
                DB.getInstance().getDrawing(json.getInt("drawing")),
                DB.getInstance().getChallenge(json.getInt("challenge")),
                json.getBoolean("is_creator"),
                json.getInt("votes")
        );
    }

    @NonNull
    public User getUser() {
        return this._user;
    }

    @NonNull
    public Drawing getDrawing() {
        return this._drawing;
    }

    @NonNull
    public Challenge getChallenge() {
        return this._challenge;
    }

    @NonNull
    public Boolean isCreator() {
        return this._isCreator;
    }

    @NonNull
    public Integer getVotes() {
        return this._votes;
    }

    /**
     * Obtention de la représentation d'une participation au format JSON (pour l'envoi dans des requêtes)
     * @return La participation au format JSONObject
     */
    @Nullable
    public JSONObject toJson() {
        try {
            return new JSONObject()
                    // Envoi des ids des 3 pour la base de données
                    .put("u_id", this._user.getUsername())
                    .put("d_id", this._drawing.getId())
                    .put("c_id", this._challenge.getId())
                    .put("is_creator", this._isCreator)
                    .put("votes", this._votes);
        } catch (JSONException e) {
            // Error lors du parsing de vers JSON
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "PARTICIPATION: [ " + this._user.getUsername() + ", " + this._drawing.getId() + ", " + this._challenge.getId() + " ](" + this._isCreator + ", " + this._votes + ")";
    }
}
