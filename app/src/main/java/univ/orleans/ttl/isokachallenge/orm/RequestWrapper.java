package univ.orleans.ttl.isokachallenge.orm;

import android.database.sqlite.SQLiteDatabase;

public class RequestWrapper {

    private SQLiteDatabase _db;

    public RequestWrapper(SQLiteDatabase db) { this._db = db; }
}
