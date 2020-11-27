package univ.orleans.ttl.isokachallenge.orm.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Drawing {

    private Integer _id;
    private final String _link;
    private final LocalDateTime _date;

    public Drawing(Integer id, String link, LocalDateTime date) {
        this._id = id;
        this._link = link;
        this._date = date;
    }

    public Drawing(String link, LocalDateTime date) {
        this(null, link, date);
    }

    public static Drawing fromJson(JSONObject json) throws JSONException {
        return new Drawing(
                json.getInt("id"),
                json.getString("link"),
                LocalDateTime.parse(json.getString("date"))
        );
    }

    public Integer getId() {
        return this._id;
    }

    public void setInt(Integer id) {
        this._id = id;
    }

    public String getLink() {
        return this._link;
    }

    public String getDate() {
        return this._date.toString();
    }

    public String getFormattedDate() {
        return this._date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH'h'mm"));
    }

    public JSONObject toJson() {
        try {
            return new JSONObject()
                    .put("id", this._id)
                    .put("link", this._link)
                    .put("date", this._date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "DRAWING: " + this._id + " (" + this._link + ", " + this._date + ")";
    }
}
