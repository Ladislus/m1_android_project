package univ.orleans.ttl.isokachallenge.orm.entity;

import java.time.LocalDateTime;

public class Challenge {

    private Integer _id;
    private final String _name;
    private final Boolean _type;
    private final String _theme;
    private final LocalDateTime _date;
    private final Integer _timer;

    public Challenge(Integer id, String name, Boolean type, String theme, LocalDateTime date, Integer timer) {
        this._id = id;
        this._name = name;
        this._type = type;
        this._theme = theme;
        this._date = date;
        this._timer = timer;
    }

    public Challenge(String name, Boolean type, String theme, LocalDateTime date, Integer timer) {
        this(null, name, type, theme, date, timer);
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

    public Integer getTimer() {
        return this._timer;
    }
}
