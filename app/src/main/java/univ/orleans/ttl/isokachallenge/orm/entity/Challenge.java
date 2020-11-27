package univ.orleans.ttl.isokachallenge.orm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Challenge {

    //DEBUG
    //TODO Remove
    private static int _count = 0;

    private Integer _id;
    private final String _name;
    private final Boolean _type;
    private final String _theme;
    private final LocalDateTime _date;
    private final Integer _timer;
    private final String _desc;

    public Challenge(Integer id, String name, Boolean type, String theme, LocalDateTime date, Integer timer, String desc) {
        this._id = id;
        this._name = name;
        this._type = type;
        this._theme = theme;
        this._date = date;
        this._timer = timer;
        this._desc = desc;
    }

    public Challenge(String name, Boolean type, String theme, LocalDateTime date, Integer timer, String desc) {
        //DEBUG
        //TODO set id to null before server request
        this(_count++, name, type, theme, date, timer, desc);
    }

    public Integer getId() {
        return this._id;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    public String getName() {
        return this._name;
    }

    public Boolean getType() {
        return this._type;
    }

    public String getTheme() {
        return this._theme;
    }

    public String getDate() {
        return this._date.toString();
    }

    public String getFormattedDate() {
        return this._date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH'h'mm"));
    }

    public Integer getTimer() {
        return this._timer;
    }

    public String getDesc() { return this._desc; }

    public JSONObject toJson() {
        try {
            return new JSONObject()
                    .put("id", this._id)
                    .put("name", this._name)
                    .put("type", this._type)
                    .put("theme", this._theme)
                    .put("desc", this._desc)
                    .put("date", this._date.toString())
                    .put("timer", this._timer);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "CHALLENGE: " + this._id + " (" + this._name + ", " + this._type + this._theme + ", " + this._date + ", " + this._timer + ", " + this._desc + ")";
    }
}
