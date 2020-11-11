package univ.orleans.ttl.isokachallenge.orm

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import univ.orleans.ttl.isokachallenge.orm.entity.*
import java.lang.StringBuilder
import java.sql.SQLException
import java.time.LocalDateTime

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

    fun save(challenge: Challenge) {
        try {
            val values = ContentValues()
            values.put(CHALLENGE_ID, challenge.id)
            values.put(CHALLENGE_NAME, challenge.name)
            values.put(CHALLENGE_TYPE, challenge.type)
            values.put(CHALLENGE_THEME, challenge.theme)
            values.put(CHALLENGE_DURATION, challenge.date)
            values.put(CHALLENGE_TIMER, challenge.timer)

            writableDatabase.insertOrThrow(CHALLENGE_TABLE, null, values)
            Log.d(DB_LOG, "SAVED CHALLENGE ${challenge.name}")
        } catch (ex: SQLException) {
            Log.e(DB_LOG, "SAVE CHALLENGE ${challenge.name} FAILED !")
            ex.message?.let { Log.e(DB_LOG, it) }
        }
    }

    fun update(challenge: Challenge) {
        val values = ContentValues()
        values.put(CHALLENGE_ID, challenge.id)
        values.put(CHALLENGE_NAME, challenge.name)
        values.put(CHALLENGE_TYPE, challenge.type)
        values.put(CHALLENGE_THEME, challenge.theme)
        values.put(CHALLENGE_DURATION, challenge.date)
        values.put(CHALLENGE_TIMER, challenge.timer)

        writableDatabase.update(CHALLENGE_TABLE, values, "$CHALLENGE_ID = ${challenge.id}", null)
    }

    fun delete(challenge: Challenge) {
        writableDatabase.delete(CHALLENGE_TABLE, "$CHALLENGE_ID = ${challenge.id}", null)
    }

    fun getAllChallenges(): Collection<Challenge> {
        val list: MutableCollection<Challenge> = ArrayList()

        writableDatabase.query(
                CHALLENGE_TABLE,
                arrayOf(CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_TYPE, CHALLENGE_THEME, CHALLENGE_DURATION, CHALLENGE_TIMER),
                null,
                null,
                null,
                null,
                "$CHALLENGE_DURATION ASC",
        ).use {
            while (it.moveToNext()) {
                list.add(Challenge(it.getInt(0), it.getString(1), it.getInt(2) > 0, it.getString(3), LocalDateTime.parse(it.getString(4)), it.getInt(5)))
            }
        }

        return list
    }

    fun getChallenges(wheres: Map<String, String>): Collection<Challenge> {
        return if (wheres.isEmpty()) getAllChallenges()
        else {
            val list: MutableCollection<Challenge> = ArrayList()

            val sb = StringBuilder("WHERE 1")
            for (s in wheres.keys) sb.append(" AND $s = ${wheres[s]}")

            writableDatabase.query(
                    CHALLENGE_TABLE,
                    arrayOf(CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_TYPE, CHALLENGE_THEME, CHALLENGE_DURATION, CHALLENGE_TIMER),
                    sb.toString(),
                    null,
                    null,
                    null,
                    "$CHALLENGE_DURATION ASC",
            ).use {
                while (it.moveToNext()) {
                    list.add(Challenge(it.getInt(0), it.getString(1), it.getInt(2) != 0, it.getString(3), LocalDateTime.parse(it.getString(4)), it.getInt(5)))
                }
            }

            list
        }
    }

    fun getChallenge(id: Int): Challenge? {
        writableDatabase.query(
                CHALLENGE_TABLE,
                arrayOf(CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_TYPE, CHALLENGE_THEME, CHALLENGE_DURATION, CHALLENGE_TIMER),
                "$CHALLENGE_ID = $id",
                null,
                null,
                null,
                null,
        ).use {
            return if (it.moveToFirst()) {
                Challenge(it.getInt(0), it.getString(1), it.getInt(2) != 0, it.getString(3), LocalDateTime.parse(it.getString(4)), it.getInt(5))
            } else {
                null
            }
        }
    }

    fun save(drawing: Drawing) {
        try {
            val values = ContentValues()
            values.put(DRAWING_ID, drawing.id)
            values.put(DRAWING_LINK, drawing.date)
            values.put(DRAWING_LINK, drawing.link)

            writableDatabase.insertOrThrow(DRAWING_TABLE, null, values)
            Log.d(DB_LOG, "SAVED DRAWING ${drawing.link}")
        } catch (ex: SQLException) {
            Log.e(DB_LOG, "SAVE DRAWING ${drawing.link} FAILED !")
            ex.message?.let { Log.e(DB_LOG, it) }
        }
    }

    fun update(drawing: Drawing) {
        val values = ContentValues()
        values.put(DRAWING_ID, drawing.id)
        values.put(DRAWING_LINK, drawing.date)
        values.put(DRAWING_LINK, drawing.link)

        writableDatabase.update(DRAWING_TABLE, values, "$DRAWING_ID = ${drawing.id}", null)
    }

    fun delete(drawing: Drawing) {
        writableDatabase.delete(DRAWING_TABLE, "$DRAWING_ID = ${drawing.id}", null)
    }

    fun getAllDrawings(): Collection<Drawing> {
        val list: MutableCollection<Drawing> = ArrayList()

        writableDatabase.query(
                DRAWING_TABLE,
                arrayOf(DRAWING_ID, DRAWING_LINK, DRAWING_CREATE),
                null,
                null,
                null,
                null,
                "$DRAWING_DATE ASC",
        ).use {
            while (it.moveToNext()) {
                list.add(Drawing(it.getInt(0), it.getString(1), LocalDateTime.parse(it.getString(2))))
            }
        }

        return list
    }

    /**
     * SELECT query on the Drawing table with a WHERE clause
     *
     * @param wheres A map of <COLUMN, VALUE> for the where clause
     * @return A Collection of all the drawing corresponding to the criteria
     */
    fun getDrawings(wheres: Map<String, String>): Collection<Drawing> {
        return if (wheres.isEmpty()) getAllDrawings()
        else {
            val list: MutableCollection<Drawing> = ArrayList()

            val sb = StringBuilder("WHERE 1")
            for (s in wheres.keys) sb.append(" AND $s = ${wheres[s]}")

            writableDatabase.query(
                    DRAWING_TABLE,
                    arrayOf(DRAWING_ID, DRAWING_LINK, DRAWING_CREATE),
                    sb.toString(),
                    null,
                    null,
                    null,
                    "$DRAWING_DATE ASC",
            ).use {
                while (it.moveToNext()) {
                    list.add(Drawing(it.getInt(0), it.getString(1), LocalDateTime.parse(it.getString(2))))
                }
            }

            list
        }
    }

    fun getDrawing(id: Int): Drawing? {
        writableDatabase.query(
                DRAWING_TABLE,
                arrayOf(DRAWING_ID, DRAWING_LINK, DRAWING_CREATE),
                "$DRAWING_ID = $id",
                null,
                null,
                null,
                null,
        ).use {
            return if (it.moveToFirst()) {
                Drawing(it.getInt(0), it.getString(1), LocalDateTime.parse(it.getString(2)))
            } else {
                null
            }
        }
    }

    fun save(participation: Participation) {
        try {
            val values = ContentValues()
            values.put(PARTICIPATION_USER_ID, participation.user.username)
            values.put(PARTICIPATION_DRAWING_ID, participation.drawing.id)
            values.put(PARTICIPATION_CHALLENGE_ID, participation.challenge.id)
            values.put(PARTICIPATION_IS_CREATOR, participation.is_creator)
            values.put(PARTICIPATION_VOTES, participation.votes)

            writableDatabase.insertOrThrow(PARTICIPATION_TABLE, null, values)
            Log.d(DB_LOG, "SAVED PARTICIPATION (${participation.user.username}, ${participation.drawing.id}, ${participation.challenge.id})")
        } catch (ex: SQLException) {
            Log.e(DB_LOG, "SAVE PARTICIPATION (${participation.user.username}, ${participation.drawing.id}, ${participation.challenge.id}) FAILED !")
            ex.message?.let { Log.e(DB_LOG, it) }
        }
    }

    fun update(participation: Participation) {
        val values = ContentValues()
        values.put(PARTICIPATION_USER_ID, participation.user.username)
        values.put(PARTICIPATION_DRAWING_ID, participation.drawing.id)
        values.put(PARTICIPATION_CHALLENGE_ID, participation.challenge.id)
        values.put(PARTICIPATION_IS_CREATOR, participation.is_creator)
        values.put(PARTICIPATION_VOTES, participation.votes)

        writableDatabase.update(PARTICIPATION_TABLE, values, "$PARTICIPATION_USER_ID = ${participation.user.username} AND $PARTICIPATION_DRAWING_ID = ${participation.drawing.id} AND $PARTICIPATION_CHALLENGE_ID = ${participation.challenge.id}", null)
    }

    fun delete(participation: Participation) {
        writableDatabase.delete(PARTICIPATION_TABLE, "$PARTICIPATION_USER_ID = ${participation.user.username} AND $PARTICIPATION_DRAWING_ID = ${participation.drawing.id} AND $PARTICIPATION_CHALLENGE_ID = ${participation.challenge.id}", null)
    }

    fun getAllParticipations(): Collection<Participation> {
        val list: MutableCollection<Participation> = ArrayList()

        writableDatabase.query(
                PARTICIPATION_TABLE,
                arrayOf(PARTICIPATION_USER_ID, PARTICIPATION_DRAWING_ID, PARTICIPATION_CHALLENGE_ID, PARTICIPATION_IS_CREATOR, PARTICIPATION_VOTES),
                null,
                null,
                null,
                null,
                "$PARTICIPATION_VOTES ASC"
        ).use {
            while (it.moveToNext()) {
                getUser(it.getString(0))?.let { it_u ->
                    getDrawing(it.getInt(1))?.let { it_d ->
                        getChallenge(it.getInt(2))?.let { it_c ->
                            list.add(Participation(it_u, it_d, it_c, it.getInt(3) != 0, it.getInt(4)))
                        }
                    }
                }
            }
        }

        return list
    }

    fun getParticipations(wheres: Map<String, String>): Collection<Participation> {
        return if (wheres.isEmpty()) getAllParticipations()
        else {
            val list: MutableCollection<Participation> = ArrayList()

            val sb = StringBuilder("WHERE 1")
            for (s in wheres.keys) sb.append(" AND $s = ${wheres[s]}")

            writableDatabase.query(
                    PARTICIPATION_TABLE,
                    arrayOf(PARTICIPATION_USER_ID, PARTICIPATION_DRAWING_ID, PARTICIPATION_CHALLENGE_ID, PARTICIPATION_IS_CREATOR, PARTICIPATION_VOTES),
                    sb.toString(),
                    null,
                    null,
                    null,
                    "$PARTICIPATION_VOTES ASC"
            ).use {
                while (it.moveToNext()) {
                    getUser(it.getString(0))?.let { it_u ->
                        getDrawing(it.getInt(1))?.let { it_d ->
                            getChallenge(it.getInt(2))?.let { it_c ->
                                list.add(Participation(it_u, it_d, it_c, it.getInt(3) != 0, it.getInt(4)))
                            }
                        }
                    }
                }
            }

            list
        }
    }

    fun getParticipation(username: String, drawing_id: Int, challenge_id: Int): Participation? {
        writableDatabase.query(
                PARTICIPATION_TABLE,
                arrayOf(PARTICIPATION_USER_ID, PARTICIPATION_DRAWING_ID, PARTICIPATION_CHALLENGE_ID, PARTICIPATION_IS_CREATOR, PARTICIPATION_VOTES),
                "$PARTICIPATION_USER_ID = $username AND $PARTICIPATION_DRAWING_ID = $drawing_id AND $PARTICIPATION_CHALLENGE_ID = $challenge_id",
                null,
                null,
                null,
                "$PARTICIPATION_VOTES ASC"
        ).use {
            return if (it.moveToFirst()) {
                getUser(it.getString(0))?.let { it_u ->
                    getDrawing(it.getInt(1))?.let { it_d ->
                        getChallenge(it.getInt(2))?.let { it_c ->
                            Participation(it_u, it_d, it_c, it.getInt(3) != 0, it.getInt(4))
                        }
                    }
                }
            } else {
                null
            }
        }
    }

    fun save(user: User) {
        try {
            val values = ContentValues()
            values.put(USER_NAME, user.username)
            values.put(USER_PASSWORD, user.password)
            values.put(USER_SALT, user.salt)
            values.put(USER_DATE, user.date)

            writableDatabase.insertOrThrow(USER_TABLE, null, values)
            Log.d(DB_LOG, "SAVED USER ${user.username}")
        } catch (ex: SQLException) {
            Log.e(DB_LOG, "SAVE USER ${user.username} FAILED !")
            ex.message?.let { Log.e(DB_LOG, it) }
        }
    }

    fun update(user: User) {
        val values = ContentValues()
        values.put(USER_NAME, user.username)
        values.put(USER_PASSWORD, user.password)
        values.put(USER_SALT, user.salt)
        values.put(USER_DATE, user.date)

        writableDatabase.update(USER_TABLE, values, "$USER_NAME = ${user.username}", null)
    }

    fun delete(user: User) {
        writableDatabase.delete(USER_TABLE, "$USER_NAME = ${user.username}", null)
    }

    fun getAllUsers() : Collection<User> {
        val list: MutableCollection<User> = ArrayList()

        writableDatabase.query(
                USER_TABLE,
                arrayOf(USER_NAME, USER_PASSWORD, USER_SALT, USER_DATE),
                null,
                null,
                null,
                null,
                "$USER_NAME ASC",
        ).use {
            while (it.moveToNext()) {
                list.add(User(it.getString(0), it.getString(1), it.getString(2), LocalDateTime.parse(it.getString(3))))
            }
        }

        return list
    }

    fun getUsers(wheres: Map<String, String>): Collection<User> {
        return if (wheres.isEmpty()) getAllUsers()
        else {
            val list: MutableCollection<User> = ArrayList()

            val sb = StringBuilder("WHERE 1")
            for (s in wheres.keys) sb.append(" AND $s = ${wheres[s]}")

            writableDatabase.query(
                    USER_TABLE,
                    arrayOf(USER_NAME, USER_PASSWORD, USER_SALT, USER_DATE),
                    sb.toString(),
                    null,
                    null,
                    null,
                    "$USER_NAME ASC",
            ).use {
                while (it.moveToNext()) {
                    list.add(User(it.getString(0), it.getString(1), it.getString(2), LocalDateTime.parse(it.getString(3))))
                }

                list
            }
        }
    }

    fun getUser(username: String): User? {
        writableDatabase.query(
                USER_TABLE,
                arrayOf(USER_NAME, USER_PASSWORD, USER_SALT, USER_DATE),
                "$USER_NAME = $username",
                null,
                null,
                null,
                null
        ).use {
            return if (it.moveToFirst()) {
                User(it.getString(0), it.getString(1), it.getString(2), LocalDateTime.parse(it.getString(3)))
            } else {
                null
            }
        }
    }
}