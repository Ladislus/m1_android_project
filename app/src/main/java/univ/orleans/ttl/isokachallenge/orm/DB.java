package univ.orleans.ttl.isokachallenge.orm;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import java.time.LocalDateTime;

import univ.orleans.ttl.isokachallenge.orm.entity.*;

public class DB extends SQLiteOpenHelper {

    private final RequestWrapper _wrapper = new RequestWrapper(this.getWritableDatabase());

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
            values.put(Tables.DRAWING_LINK, drawing.getLink());
            values.put(Tables.DRAWING_DATE, drawing.getDate());

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
            values.put(Tables.CHALLENGE_DESCRIPTION, challenge.getDesc());

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

        int nbAffected = this.getWritableDatabase().update(Tables.USER_TABLE, values, Tables.USER_NAME + " = ?", new String[] { user.getUsername() });
        Log.d(Tables.DB_LOG, "(USER) NUMBER OF ROWS AFFECTED: " + nbAffected);
        return nbAffected;
    }

    public int update(Drawing drawing) {
        ContentValues values = new ContentValues();
        values.put(Tables.DRAWING_ID, drawing.getId());
        values.put(Tables.DRAWING_LINK, drawing.getLink());
        values.put(Tables.DRAWING_DATE, drawing.getDate());

        int nbAffected = this.getWritableDatabase().update(Tables.DRAWING_TABLE, values, Tables.DRAWING_ID + " = ?", new String[] { String.valueOf(drawing.getId()) });
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
        values.put(Tables.CHALLENGE_DESCRIPTION, challenge.getDesc());

        int nbAffected = this.getWritableDatabase().update(Tables.CHALLENGE_TABLE, values, Tables.CHALLENGE_ID + " = ?", new String[] { String.valueOf(challenge.getId()) });
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

        int nbAffected = this.getWritableDatabase().update(Tables.PARTICIPATION_TABLE, values, Tables.PARTICIPATION_USER_ID + " = ? AND " + Tables.PARTICIPATION_DRAWING_ID + " = ? AND " + Tables.PARTICIPATION_CHALLENGE_ID + " = ?", new String[] { participation.getUser().getUsername(), String.valueOf(participation.getDrawing().getId()), String.valueOf(participation.getChallenge().getId()) });
        Log.d(Tables.DB_LOG, "(PARTICIPATION) NUMBER OF ROWS AFFECTED: " + nbAffected);
        return nbAffected;
    }

    //////////////////////////////
    //          DELETE          //
    //////////////////////////////

    public int delete(User user) {
        int nbAffected = this.getWritableDatabase().delete(Tables.USER_TABLE, Tables.USER_NAME + " = ?", new String[] { user.getUsername() });
        Log.d(Tables.DB_LOG, "(USER) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    public int delete(Drawing drawing) {
        int nbAffected = this.getWritableDatabase().delete(Tables.DRAWING_TABLE, Tables.DRAWING_ID + " = ?", new String[] { String.valueOf(drawing.getId()) });
        Log.d(Tables.DB_LOG, "(DRAWING) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    public int delete(Challenge challenge) {
        int nbAffected = this.getWritableDatabase().delete(Tables.CHALLENGE_TABLE, Tables.CHALLENGE_ID + " = ?", new String[] { String.valueOf(challenge.getId() )});
        Log.d(Tables.DB_LOG, "(CHALLENGE) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    public int delete(Participation participation) {
        int nbAffected =  this.getWritableDatabase().delete(Tables.PARTICIPATION_TABLE, Tables.PARTICIPATION_USER_ID + " = ? AND " + Tables.PARTICIPATION_DRAWING_ID + " = ? AND " + Tables.PARTICIPATION_CHALLENGE_ID + " = ?", new String[] { participation.getUser().getUsername(), String.valueOf(participation.getDrawing().getId()), String.valueOf(participation.getChallenge().getId()) });
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
               User returned = new User(c.getString(Tables.USER_NAME_INDEX), LocalDateTime.parse(c.getString(Tables.USER_DATE_INDEX)));
               Log.d(Tables.DB_LOG, "(USER) FOUND " + returned);
               return returned;
           } else {
               Log.d(Tables.DB_LOG, "(USER) NOTHING FOUND");
               return null;
           }
        }
    }

    public Drawing getDrawing(int id) {
        try (
                Cursor c = getWritableDatabase().query(
                        Tables.DRAWING_TABLE,
                        new String[] { Tables.DRAWING_ID, Tables.DRAWING_LINK, Tables.DRAWING_DATE },
                        Tables.DRAWING_ID + " = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null
                )
        ) {
            if (c.moveToFirst()) {
                Drawing returned = new Drawing(c.getInt(Tables.DRAWING_ID_INDEX), c.getString(Tables.DRAWING_LINK_INDEX), LocalDateTime.parse(c.getString(Tables.DRAWING_DATE_INDEX)));
                Log.d(Tables.DB_LOG, "(DRAWING) FOUND " + returned);
                return returned;
            } else {
                Log.d(Tables.DB_LOG, "(DRAWING) NOTHING FOUND");
                return null;
            }
        }
    }

    public Challenge getChallenge(int id) {
        try (
                Cursor c = getWritableDatabase().query(
                        Tables.CHALLENGE_TABLE,
                        new String[] { Tables.CHALLENGE_ID, Tables.CHALLENGE_NAME, Tables.CHALLENGE_TYPE, Tables.CHALLENGE_THEME, Tables.CHALLENGE_DURATION, Tables.CHALLENGE_TIMER, Tables.CHALLENGE_DESCRIPTION },
                        Tables.CHALLENGE_ID + " = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null
                )
        ) {
            if (c.moveToFirst()) {
                Challenge returned = new Challenge(c.getInt(Tables.CHALLENGE_ID_INDEX), c.getString(Tables.CHALLENGE_NAME_INDEX), c.getInt(Tables.CHALLENGE_TYPE_INDEX) > 0, c.getString(Tables.CHALLENGE_THEME_INDEX), LocalDateTime.parse(c.getString(Tables.CHALLENGE_DURATION_INDEX)), c.getInt(Tables.CHALLENGE_TIMER_INDEX), c.getString(Tables.CHALLENGE_DESCRIPTION_INDEX));
                Log.d(Tables.DB_LOG, "(CHALLENGE) FOUND " + returned);
                return returned;
            } else {
                Log.d(Tables.DB_LOG, "(USER) NOTHING FOUND");
                return null;
            }
        }
    }

    public Participation getParticipation(String id_user, int id_drawing, int id_challenge) {
        try (
                Cursor c = getWritableDatabase().query(
                        Tables.PARTICIPATION_TABLE,
                        new String[] { Tables.PARTICIPATION_USER_ID, Tables.PARTICIPATION_DRAWING_ID, Tables.PARTICIPATION_CHALLENGE_ID, Tables.PARTICIPATION_IS_CREATOR, Tables.PARTICIPATION_VOTES },
                        Tables.PARTICIPATION_USER_ID + " = ? AND " + Tables.PARTICIPATION_DRAWING_ID + " = ? AND " + Tables.PARTICIPATION_CHALLENGE_ID + " = ?",
                        new String[] { id_user, String.valueOf(id_drawing), String.valueOf(id_challenge) },
                        null,
                        null,
                        null
                )
        ) {
            if (c.moveToFirst()) {
                Participation returned = new Participation(getUser(c.getString(Tables.PARTICIPATION_USER_ID_INDEX)), getDrawing(c.getInt(Tables.PARTICIPATION_DRAWING_ID_INDEX)), getChallenge(c.getInt(Tables.PARTICIPATION_CHALLENGE_ID_INDEX)), c.getInt(Tables.PARTICIPATION_IS_CREATOR_INDEX) > 0, c.getInt(Tables.PARTICIPATION_VOTES_INDEX));
                Log.d(Tables.DB_LOG, "(PARTICIPATION) FOUND " + returned);
                return returned;
            } else {
                Log.d(Tables.DB_LOG, "(USER) NOTHING FOUND");
                return null;
            }
        }
    }

    //////////////////////////////
    //          GETALL          //
    //////////////////////////////

    public Collection<User> getAllUsers() {
        Collection<User> users = new ArrayList<>();

        try (
                Cursor c = getWritableDatabase().query(
                        Tables.USER_TABLE,
                        new String[] { Tables.USER_NAME, Tables.USER_DATE },
                        null,
                        null,
                        null,
                        null,
                        null
                )
        ) {
            while (c.moveToNext()) {
                users.add(new User(c.getString(Tables.USER_NAME_INDEX), LocalDateTime.parse(c.getString(Tables.USER_DATE_INDEX))));
            }
        }
        StringBuilder sb = new StringBuilder("ALL USERS :\n\t[\n");
        for (User u : users) sb.append("\t\t").append(u).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return users;
    }

    public Collection<Drawing> getAllDrawings() {
        Collection<Drawing> drawings = new ArrayList<>();

        try (
                Cursor c = getWritableDatabase().query(
                        Tables.DRAWING_TABLE,
                        new String[] { Tables.DRAWING_ID, Tables.DRAWING_LINK, Tables.DRAWING_DATE },
                        null,
                        null,
                        null,
                        null,
                        null
                )
        ) {
            while (c.moveToNext()) {
                drawings.add(new Drawing(c.getInt(Tables.DRAWING_ID_INDEX), c.getString(Tables.DRAWING_LINK_INDEX), LocalDateTime.parse(c.getString(Tables.DRAWING_DATE_INDEX))));
            }
        }
        StringBuilder sb = new StringBuilder("ALL DRAWINGS :\n\t[\n");
        for (Drawing d : drawings) sb.append("\t\t").append(d).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return drawings;
    }

    public Collection<Challenge> getAllChallenges() {
        Collection<Challenge> challenges = new ArrayList<>();

        try (
                Cursor c = getWritableDatabase().query(
                        Tables.CHALLENGE_TABLE,
                        new String[] { Tables.CHALLENGE_ID, Tables.CHALLENGE_NAME, Tables.CHALLENGE_TYPE, Tables.CHALLENGE_THEME, Tables.CHALLENGE_DURATION, Tables.CHALLENGE_TIMER, Tables.CHALLENGE_DESCRIPTION },
                        null,
                        null,
                        null,
                        null,
                        null
                )
        ) {
            while (c.moveToNext()) {
                challenges.add(new Challenge(c.getInt(Tables.CHALLENGE_ID_INDEX), c.getString(Tables.CHALLENGE_NAME_INDEX), c.getInt(Tables.CHALLENGE_TYPE_INDEX) > 0, c.getString(Tables.CHALLENGE_THEME_INDEX), LocalDateTime.parse(c.getString(Tables.CHALLENGE_DURATION_INDEX)), c.getInt(Tables.CHALLENGE_TIMER_INDEX), c.getString(Tables.CHALLENGE_DESCRIPTION_INDEX)));
            }
        }
        StringBuilder sb = new StringBuilder("ALL CHALLENGES :\n\t[\n");
        for (Challenge c : challenges) sb.append("\t\t").append(c).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return challenges;
    }

    public Collection<Participation> getAllParticipations() {
        Collection<Participation> participations = new ArrayList<>();

        try (
                Cursor c = getWritableDatabase().query(
                        Tables.PARTICIPATION_TABLE,
                        new String[] { Tables.PARTICIPATION_USER_ID, Tables.PARTICIPATION_DRAWING_ID, Tables.PARTICIPATION_CHALLENGE_ID, Tables.PARTICIPATION_IS_CREATOR, Tables.PARTICIPATION_VOTES },
                        null,
                        null,
                        null,
                        null,
                        null
                )
        ) {
            while (c.moveToNext()) {
                 participations.add(new Participation(getUser(c.getString(Tables.PARTICIPATION_USER_ID_INDEX)), getDrawing(c.getInt(Tables.PARTICIPATION_DRAWING_ID_INDEX)), getChallenge(c.getInt(Tables.PARTICIPATION_CHALLENGE_ID_INDEX)), c.getInt(Tables.PARTICIPATION_IS_CREATOR_INDEX) > 0, c.getInt(Tables.PARTICIPATION_VOTES_INDEX)));
            }
        }
        StringBuilder sb = new StringBuilder("ALL PARTICIPATIONS :\n\t[\n");
        for (Participation p : participations) sb.append("\t\t").append(p).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return participations;
    }

    //////////////////////////////
    //         GET WHERE        //
    //////////////////////////////

    public Collection<User> getUsers(Map<String, Pair<String, String>> wheres) {

        if (Objects.isNull(wheres) || wheres.isEmpty()) return getAllUsers();

        Collection<User> users = new ArrayList<>();

        StringBuilder query = new StringBuilder("1");
        String[] args = new String[wheres.size()];

        int index = 0;
        for (String key : wheres.keySet()) {
            query.append(" AND ").append(key).append(wheres.get(key).first).append("?");
            args[index++] = wheres.get(key).second;
        }

        try (
                Cursor c = getWritableDatabase().query(
                        Tables.USER_TABLE,
                        new String[] { Tables.USER_NAME, Tables.USER_DATE },
                        query.toString(),
                        args,
                        null,
                        null,
                        null
                )
        ) {
            while (c.moveToNext()) {
                users.add(new User(c.getString(Tables.USER_NAME_INDEX), LocalDateTime.parse(c.getString(Tables.USER_DATE_INDEX))));
            }
        }
        StringBuilder sb = new StringBuilder("USERS FOUND :\n\t[\n");
        for (User u : users) sb.append("\t\t").append(u).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return users;
    }

    public Collection<Drawing> getDrawings(Map<String, Pair<String, String>> wheres) {

        if (Objects.isNull(wheres) || wheres.isEmpty()) return getAllDrawings();

        Collection<Drawing> drawings = new ArrayList<>();

        StringBuilder query = new StringBuilder("1");
        String[] args = new String[wheres.size()];

        int index = 0;
        for (String key : wheres.keySet()) {
            query.append(" AND ").append(key).append(wheres.get(key).first).append("?");
            args[index++] = wheres.get(key).second;
        }

        try (
                Cursor c = getWritableDatabase().query(
                        Tables.DRAWING_TABLE,
                        new String[] { Tables.DRAWING_ID, Tables.DRAWING_LINK, Tables.DRAWING_DATE },
                        query.toString(),
                        args,
                        null,
                        null,
                        null
                )
        ) {
            while (c.moveToNext()) {
                drawings.add(new Drawing(c.getInt(Tables.DRAWING_ID_INDEX), c.getString(Tables.DRAWING_LINK_INDEX), LocalDateTime.parse(c.getString(Tables.DRAWING_DATE_INDEX))));
            }
        }
        StringBuilder sb = new StringBuilder("DRAWINGS FOUND :\n\t[\n");
        for (Drawing d : drawings) sb.append("\t\t").append(d).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return drawings;
    }

    public Collection<Challenge> getChallenges(Map<String, Pair<String, String>> wheres) {

        if (Objects.isNull(wheres) || wheres.isEmpty()) return getAllChallenges();

        Collection<Challenge> challenges = new ArrayList<>();

        StringBuilder query = new StringBuilder("1");
        String[] args = new String[wheres.size()];

        int index = 0;
        for (String key : wheres.keySet()) {
            query.append(" AND ").append(key).append(wheres.get(key).first).append("?");
            args[index++] = wheres.get(key).second;
        }

        try (
                Cursor c = getWritableDatabase().query(
                        Tables.CHALLENGE_TABLE,
                        new String[] { Tables.CHALLENGE_ID, Tables.CHALLENGE_NAME, Tables.CHALLENGE_TYPE, Tables.CHALLENGE_THEME, Tables.CHALLENGE_DURATION, Tables.CHALLENGE_TIMER, Tables.CHALLENGE_DESCRIPTION },
                        query.toString(),
                        args,
                        null,
                        null,
                        null
                )
        ) {
            while (c.moveToNext()) {
                challenges.add(new Challenge(c.getInt(Tables.DRAWING_ID_INDEX), c.getString(Tables.CHALLENGE_NAME_INDEX), c.getInt(Tables.CHALLENGE_TYPE_INDEX) > 0, c.getString(Tables.CHALLENGE_THEME_INDEX), LocalDateTime.parse(c.getString(Tables.CHALLENGE_DURATION_INDEX)), c.getInt(Tables.CHALLENGE_TIMER_INDEX), c.getString(Tables.CHALLENGE_DESCRIPTION_INDEX)));
            }
        }
        StringBuilder sb = new StringBuilder("CHALLENGES FOUND :\n\t[\n");
        for (Challenge c : challenges) sb.append("\t\t").append(c).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return challenges;
    }

    public Collection<Participation> getParticipations(Map<String, Pair<String, String>> wheres) {

        if (Objects.isNull(wheres) || wheres.isEmpty()) return getAllParticipations();

        Collection<Participation> participations = new ArrayList<>();

        StringBuilder query = new StringBuilder("1");
        String[] args = new String[wheres.size()];

        int index = 0;
        for (String key : wheres.keySet()) {
            query.append(" AND ").append(key).append(wheres.get(key).first).append("?");
            args[index++] = wheres.get(key).second;
        }

        try (
                Cursor c = getWritableDatabase().query(
                        Tables.PARTICIPATION_TABLE,
                        new String[] { Tables.PARTICIPATION_USER_ID, Tables.PARTICIPATION_DRAWING_ID, Tables.PARTICIPATION_CHALLENGE_ID, Tables.PARTICIPATION_IS_CREATOR, Tables.PARTICIPATION_VOTES },
                        query.toString(),
                        args,
                        null,
                        null,
                        null
                )
        ) {
            while (c.moveToNext()) {
                participations.add(new Participation(getUser(c.getString(Tables.PARTICIPATION_USER_ID_INDEX)), getDrawing(c.getInt(Tables.PARTICIPATION_DRAWING_ID_INDEX)), getChallenge(c.getInt(Tables.PARTICIPATION_CHALLENGE_ID_INDEX)), c.getInt(Tables.PARTICIPATION_IS_CREATOR_INDEX) > 0, c.getInt(Tables.PARTICIPATION_VOTES_INDEX)));
            }
        }
        StringBuilder sb = new StringBuilder("PARTICIPATIONS FOUND :\n\t[\n");
        for (Participation p : participations) sb.append("\t\t").append(p).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return participations;
    }
}
