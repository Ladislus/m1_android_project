package univ.orleans.ttl.isokachallenge.orm.entity;

import java.time.LocalDateTime;

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

    @Override
    public String toString() {
        return "DRAWING: " + this._id + " (" + this._link + ", " + this._date + ")";
    }
}