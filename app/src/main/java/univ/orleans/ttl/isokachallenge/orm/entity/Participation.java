package univ.orleans.ttl.isokachallenge.orm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import univ.orleans.ttl.isokachallenge.orm.DB;

public class Participation {

    private final User _user;
    private final Drawing _drawing;
    private final Challenge _challenge;
    private final Boolean _isCreator;
    private Integer _votes;

    public Participation(User user, Drawing drawing, Challenge challenge, Boolean isCreator, Integer votes) {
        this._user = user;
        this._drawing = drawing;
        this._challenge = challenge;
        this._isCreator = isCreator;
        this._votes = votes;
    }

    public Participation(User user, Drawing drawing, Challenge challenge, Boolean isCreator) {
        this(user, drawing, challenge, isCreator, 0);
    }

    public static Participation fromJson(JSONObject json) throws JSONException {
        return new Participation(
                DB.getInstance().getUser(json.getString("user")),
                DB.getInstance().getDrawing(json.getInt("drawing")),
                DB.getInstance().getChallenge(json.getInt("challenge")),
                json.getBoolean("is_creator"),
                json.getInt("votes")
        );
    }

    public User getUser() {
        return this._user;
    }

    public Drawing getDrawing() {
        return this._drawing;
    }

    public Challenge getChallenge() {
        return this._challenge;
    }

    public Boolean isCreator() {
        return this._isCreator;
    }

    public Integer getVotes() {
        return this._votes;
    }

    @Override
    public String toString() {
        return "PARTICIPATION: [ " + this._user.getUsername() + ", " + this._drawing.getId() + ", " + this._challenge.getId() + " ](" + this._isCreator + ", " + this._votes + ")";
    }

    public JSONObject toJson() {
        try {
            return new JSONObject()
                    .put("u_id", this._user.getUsername())
                    .put("d_id", this._drawing.getId())
                    .put("c_id", this._challenge.getId())
                    .put("is_creator", this._isCreator)
                    .put("votes", this._votes);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addVote() {
        return ++this._votes;
    }
}
