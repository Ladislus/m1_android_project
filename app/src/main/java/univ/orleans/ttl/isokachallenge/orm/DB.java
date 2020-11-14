package univ.orleans.ttl.isokachallenge.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.time.LocalDateTime;
import java.util.Arrays;

import univ.orleans.ttl.isokachallenge.orm.entity.*;

public class DB extends SQLiteOpenHelper {

    public DB(Context context) {
        super(context, Tables.DB_NAME, null, Tables.DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tables.USER_CREATE);
        db.execSQL(Tables.DRAWING_CREATE);
        db.execSQL(Tables.CHALLENGE_CREATE);
        db.execSQL(Tables.PARTICIPATION_CREATE);

        Log.d(Tables.DB_LOG, "Database created !");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Tables.DROP);
        this.onCreate(db);
    }

    //////////////////////////////
    //          SAVE            //
    //////////////////////////////

    public boolean save(User user) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.USER_NAME, user.getUsername());
            values.put(Tables.USER_DATE, user.getDate());

            this.getWritableDatabase().insertOrThrow(Tables.USER_TABLE, null, values);
            Log.d(Tables.DB_LOG, "SAVED " + user);
            return true;
        } catch (SQLException ex) {
            Log.e(Tables.DB_LOG, "COULDN'T SAVE " + user + "\n\tDETAILS :\n\t\t" + ex.getMessage());
            return false;
        }
    }

    public boolean save(Drawing drawing) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.DRAWING_ID, drawing.getId());
            values.put(Tables.DRAWING_LINK, drawing.getDate());
            values.put(Tables.DRAWING_DATE, drawing.getLink());

            this.getWritableDatabase().insertOrThrow(Tables.DRAWING_TABLE, null, values);
            Log.d(Tables.DB_LOG, "SAVED " + drawing);
            return true;
        } catch (SQLException ex) {
            Log.e(Tables.DB_LOG, "COULDN'T SAVE " + drawing + "\n\tDETAILS :\n\t\t" + ex.getMessage());
            return false;
        }
    }

    public boolean save(Challenge challenge) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.CHALLENGE_ID, challenge.getId());
            values.put(Tables.CHALLENGE_NAME, challenge.getName());
            values.put(Tables.CHALLENGE_TYPE, challenge.getType());
            values.put(Tables.CHALLENGE_THEME, challenge.getTheme());
            values.put(Tables.CHALLENGE_DURATION, challenge.getDate());
            values.put(Tables.CHALLENGE_TIMER, challenge.getTimer());

            this.getWritableDatabase().insertOrThrow(Tables.CHALLENGE_TABLE, null, values);
            Log.d(Tables.DB_LOG, "SAVED " + challenge);
            return true;
        } catch (SQLException ex) {
            Log.e(Tables.DB_LOG, "COULDN'T SAVE " + challenge + "\n\tDETAILS :\n\t\t" + ex.getMessage());
            return false;
        }
    }

    public boolean save(Participation participation) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.PARTICIPATION_USER_ID, participation.getUser().getUsername());
            values.put(Tables.PARTICIPATION_DRAWING_ID, participation.getDrawing().getId());
            values.put(Tables.PARTICIPATION_CHALLENGE_ID, participation.getChallenge().getId());
            values.put(Tables.PARTICIPATION_IS_CREATOR, participation.isCreator());
            values.put(Tables.PARTICIPATION_VOTES, participation.getVotes());

            this.getWritableDatabase().insertOrThrow(Tables.PARTICIPATION_TABLE, null, values);
            Log.d(Tables.DB_LOG, "SAVED " + participation);
            return true;
        } catch (SQLException ex) {
            Log.e(Tables.DB_LOG, "COULDN'T SAVE " + participation + "\n\tDETAILS :\n\t\t" + ex.getMessage());
            return false;
        }
    }

    //////////////////////////////
    //          UPDATE          //
    //////////////////////////////

    public int update(User user) {
        ContentValues values = new ContentValues();
        values.put(Tables.USER_NAME, user.getUsername());
        values.put(Tables.USER_DATE, user.getDate());

        int nbAffected = this.getWritableDatabase().update(Tables.USER_TABLE, values, Tables.USER_NAME + " = \"" + user.getUsername() + "\"", null);
        Log.d(Tables.DB_LOG, "(USER) NUMBER OF ROWS AFFECTED: " + nbAffected);
        return nbAffected;
    }

    public int update(Drawing drawing) {
        ContentValues values = new ContentValues();
        values.put(Tables.DRAWING_ID, drawing.getId());
        values.put(Tables.DRAWING_LINK, drawing.getLink());
        values.put(Tables.DRAWING_DATE, drawing.getDate());

        int nbAffected = this.getWritableDatabase().update(Tables.DRAWING_TABLE, values, Tables.DRAWING_ID + " = " + drawing.getId(), null);
        Log.d(Tables.DB_LOG, "(DRAWING) NUMBER OF ROWS AFFECTED: " + nbAffected);
        return nbAffected;
    }

    public int update(Challenge challenge) {
        ContentValues values = new ContentValues();
        values.put(Tables.CHALLENGE_ID, challenge.getId());
        values.put(Tables.CHALLENGE_NAME, challenge.getName());
        values.put(Tables.CHALLENGE_TYPE, challenge.getType());
        values.put(Tables.CHALLENGE_THEME, challenge.getTheme());
        values.put(Tables.CHALLENGE_DURATION, challenge.getDate());
        values.put(Tables.CHALLENGE_TIMER, challenge.getTimer());

        int nbAffected = this.getWritableDatabase().update(Tables.CHALLENGE_TABLE, values, Tables.CHALLENGE_ID + " = " + challenge.getId(), null);
        Log.d(Tables.DB_LOG, "(CHALLENGE) NUMBER OF ROWS AFFECTED: " + nbAffected);
        return nbAffected;
    }

    public int update(Participation participation) {
        ContentValues values = new ContentValues();
        values.put(Tables.PARTICIPATION_USER_ID, participation.getUser().getUsername());
        values.put(Tables.PARTICIPATION_DRAWING_ID, participation.getDrawing().getId());
        values.put(Tables.PARTICIPATION_CHALLENGE_ID, participation.getChallenge().getId());
        values.put(Tables.PARTICIPATION_IS_CREATOR, participation.isCreator());
        values.put(Tables.PARTICIPATION_VOTES, participation.getVotes());

        int nbAffected = this.getWritableDatabase().update(Tables.PARTICIPATION_TABLE, values, Tables.PARTICIPATION_USER_ID + " = \"" + participation.getUser().getUsername() + "\" AND " + Tables.PARTICIPATION_DRAWING_ID + " = " + participation.getDrawing().getId() + " AND " + Tables.PARTICIPATION_CHALLENGE_ID + " = " + participation.getChallenge().getId(), null);
        Log.d(Tables.DB_LOG, "(PARTICIPATION) NUMBER OF ROWS AFFECTED: " + nbAffected);
        return nbAffected;
    }

    //////////////////////////////
    //          DELETE          //
    //////////////////////////////

    public int delete(User user) {
        int nbAffected = this.getWritableDatabase().delete(Tables.USER_TABLE, Tables.USER_NAME + " = \"" + user.getUsername() + "\"", null);
        Log.d(Tables.DB_LOG, "(USER) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    public int delete(Drawing drawing) {
        int nbAffected = this.getWritableDatabase().delete(Tables.DRAWING_TABLE, Tables.DRAWING_ID + " = " + drawing.getId(), null);
        Log.d(Tables.DB_LOG, "(DRAWING) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    public int delete(Challenge challenge) {
        int nbAffected = this.getWritableDatabase().delete(Tables.CHALLENGE_TABLE, Tables.CHALLENGE_ID + " = " + challenge.getId(), null);
        Log.d(Tables.DB_LOG, "(CHALLENGE) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    public int delete(Participation participation) {
        int nbAffected =  this.getWritableDatabase().delete(Tables.PARTICIPATION_TABLE, Tables.PARTICIPATION_USER_ID + " = \"" + participation.getUser().getUsername() + "\" AND " + Tables.PARTICIPATION_DRAWING_ID + " = " + participation.getDrawing().getId() + " AND " + Tables.PARTICIPATION_CHALLENGE_ID + " = " + participation.getChallenge().getId(), null);
        Log.d(Tables.DB_LOG, "(PARTICIPATION) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    //////////////////////////////
    //            GET           //
    //////////////////////////////

    public User getUser(String username) {
        try (
            Cursor c = getWritableDatabase().query(
                Tables.USER_TABLE,
                new String[] { Tables.USER_NAME, Tables.USER_DATE },
                Tables.USER_NAME + " = ?",
                new String[] { username },
                null,
                null,
                null
            )
        ) {
           if (c.moveToFirst()) {
               return new User(c.getString(0), LocalDateTime.parse(c.getString(1)));
           }
        }
        return null;
    }
}
