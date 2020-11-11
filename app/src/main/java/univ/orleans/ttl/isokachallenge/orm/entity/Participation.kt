package univ.orleans.ttl.isokachallenge.orm.entity

import android.content.ContentValues
import android.util.Log
import univ.orleans.ttl.isokachallenge.orm.*
import java.lang.StringBuilder
import java.sql.SQLException

class Participation(internal val user: User, internal val drawing: Drawing, internal val challenge: Challenge, internal val is_creator: Boolean, internal var votes: Int = 0)

fun DB.save(participation: Participation) {
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

fun DB.update(participation: Participation) {
    val values = ContentValues()
    values.put(PARTICIPATION_USER_ID, participation.user.username)
    values.put(PARTICIPATION_DRAWING_ID, participation.drawing.id)
    values.put(PARTICIPATION_CHALLENGE_ID, participation.challenge.id)
    values.put(PARTICIPATION_IS_CREATOR, participation.is_creator)
    values.put(PARTICIPATION_VOTES, participation.votes)

    writableDatabase.update(PARTICIPATION_TABLE, values, "$PARTICIPATION_USER_ID = ${participation.user.username} AND $PARTICIPATION_DRAWING_ID = ${participation.drawing.id} AND $PARTICIPATION_CHALLENGE_ID = ${participation.challenge.id}", null)
}

fun DB.delete(participation: Participation) {
    writableDatabase.delete(PARTICIPATION_TABLE, "$PARTICIPATION_USER_ID = ${participation.user.username} AND $PARTICIPATION_DRAWING_ID = ${participation.drawing.id} AND $PARTICIPATION_CHALLENGE_ID = ${participation.challenge.id}", null)
}

fun DB.getAllParticipations(): Collection<Participation> {
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

fun DB.getParticipations(wheres: Map<String, String>): Collection<Participation> {
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

fun DB.getParticipation(username: String, drawing_id: Int, challenge_id: Int): Participation? {
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