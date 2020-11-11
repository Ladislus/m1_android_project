package univ.orleans.ttl.isokachallenge.orm

import android.content.Context
import android.util.Log
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(USER_CREATE)
        db?.execSQL(DRAWING_CREATE)
        db?.execSQL(CHALLENGE_CREATE)
        db?.execSQL(PARTICIPATION_CREATE)

        Log.d(DB_LOG, "Database created !")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP)
        this.onCreate(db)
    }
}