package univ.orleans.ttl.isokachallenge.orm.entity

import android.content.ContentValues
import android.util.Log
import univ.orleans.ttl.isokachallenge.orm.*
import java.lang.StringBuilder
import java.sql.SQLException
import java.time.LocalDateTime

class Challenge(internal var id: Int?, internal var name: String, internal val type: Boolean, internal var theme: String, date: LocalDateTime, internal var timer: Int = 0) {
    private val _date: LocalDateTime = date
    internal val date: String get() = this._date.toString()

    constructor(name: String, type: Boolean, theme: String, date: LocalDateTime, timer: Int) : this(null, name, type, theme, date)
}

fun DB.save(challenge: Challenge) {
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

fun DB.update(challenge: Challenge) {
    val values = ContentValues()
    values.put(CHALLENGE_ID, challenge.id)
    values.put(CHALLENGE_NAME, challenge.name)
    values.put(CHALLENGE_TYPE, challenge.type)
    values.put(CHALLENGE_THEME, challenge.theme)
    values.put(CHALLENGE_DURATION, challenge.date)
    values.put(CHALLENGE_TIMER, challenge.timer)

    writableDatabase.update(CHALLENGE_TABLE, values, "$CHALLENGE_ID = ${challenge.id}", null)
}

fun DB.delete(challenge: Challenge) {
    writableDatabase.delete(CHALLENGE_TABLE, "$CHALLENGE_ID = ${challenge.id}", null)
}

fun DB.getAllChallenges(): Collection<Challenge> {
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

fun DB.getChallenges(wheres: Map<String, String>): Collection<Challenge> {
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

fun DB.getChallenge(id: Int): Challenge? {
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