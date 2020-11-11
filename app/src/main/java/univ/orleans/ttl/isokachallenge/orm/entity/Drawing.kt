package univ.orleans.ttl.isokachallenge.orm.entity

import android.content.ContentValues
import android.util.Log
import univ.orleans.ttl.isokachallenge.orm.*
import java.lang.StringBuilder
import java.sql.SQLException
import java.time.LocalDateTime

class Drawing(internal var id: Int?, internal var link: String, date: LocalDateTime) {
    private val _date: LocalDateTime = date
    internal val date: String get() = this._date.toString()

    constructor(link: String, date: LocalDateTime) : this(null, link, date)
}

fun DB.save(drawing: Drawing) {
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

fun DB.update(drawing: Drawing) {
    val values = ContentValues()
    values.put(DRAWING_ID, drawing.id)
    values.put(DRAWING_LINK, drawing.date)
    values.put(DRAWING_LINK, drawing.link)

    writableDatabase.update(DRAWING_TABLE, values, "$DRAWING_ID = ${drawing.id}", null)
}

fun DB.delete(drawing: Drawing) {
    writableDatabase.delete(DRAWING_TABLE, "$DRAWING_ID = ${drawing.id}", null)
}

fun DB.getAllDrawings(): Collection<Drawing> {
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
fun DB.getDrawings(wheres: Map<String, String>): Collection<Drawing> {
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

fun DB.getDrawing(id: Int): Drawing? {
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