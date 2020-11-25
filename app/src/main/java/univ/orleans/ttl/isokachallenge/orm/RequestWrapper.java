package univ.orleans.ttl.isokachallenge.orm;

import android.database.sqlite.SQLiteDatabase;

public class RequestWrapper {

    private static final String imgurAPI = "https://api.imgur.com/3/image";
    private static final String clientId = "e70fe5ed4d03b66";
    private static final String serverAPI = "https://thlato.pythonanywhere.com/api/";

    private final SQLiteDatabase _db;

    public RequestWrapper(SQLiteDatabase db) { this._db = db; }

    public String imgurUpload(String b64Image) {
        return "";
    }
}

