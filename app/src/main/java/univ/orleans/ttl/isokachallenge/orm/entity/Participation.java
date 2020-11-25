package univ.orleans.ttl.isokachallenge.orm.entity;

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

    public int addVote() {
        return ++this._votes;
    }
}
