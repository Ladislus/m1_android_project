package univ.orleans.ttl.isokachallenge.orm;

import android.database.sqlite.SQLiteDatabase;

public class RequestWrapper {

    private static final String imgurAddr = "https://api.imgur.com/3/";

    private SQLiteDatabase _db;

    public RequestWrapper(SQLiteDatabase db) { this._db = db; }

    public String imgurUpload(String b64Image) {

        return "";
    }
}
