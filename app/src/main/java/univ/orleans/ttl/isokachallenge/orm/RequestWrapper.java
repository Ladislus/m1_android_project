package univ.orleans.ttl.isokachallenge.orm;

import android.database.sqlite.SQLiteDatabase;

public class RequestWrapper {

    private static final String imgurAPI = "https://api.imgur.com/3/";
    private static final String serverAPI = "";

    private SQLiteDatabase _db;

    public RequestWrapper(SQLiteDatabase db) { this._db = db; }

    public String imgurUpload(String b64Image) {

        return "";
    }
}
