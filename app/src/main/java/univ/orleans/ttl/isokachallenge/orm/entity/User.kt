package univ.orleans.ttl.isokachallenge.orm.entity

import android.content.ContentValues
import android.util.Log
import org.springframework.security.crypto.bcrypt.BCrypt
import univ.orleans.ttl.isokachallenge.orm.*
import java.lang.StringBuilder
import java.sql.SQLException
import java.time.LocalDateTime

class User(internal var username: String, password: String, salt: String?, date: LocalDateTime) {

    internal var salt: String = salt ?: BCrypt.gensalt(12)
    internal var password: String = if (salt == null) BCrypt.hashpw(password, this.salt)  else password

    private val _date: LocalDateTime = date
    internal val date : String get() = this._date.toString()

    constructor(username: String, password: String, date: LocalDateTime) : this(username, password, null, date)
}

fun DB.save(user: User) {
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

fun DB.update(user: User) {
    val values = ContentValues()
    values.put(USER_NAME, user.username)
    values.put(USER_PASSWORD, user.password)
    values.put(USER_SALT, user.salt)
    values.put(USER_DATE, user.date)

    writableDatabase.update(USER_TABLE, values, "$USER_NAME = ${user.username}", null)
}

fun DB.delete(user: User) {
    writableDatabase.delete(USER_TABLE, "$USER_NAME = ${user.username}", null)
}

fun DB.getAllUsers() : Collection<User> {
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

fun DB.getUsers(wheres: Map<String, String>): Collection<User> {
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

fun DB.getUser(username: String): User? {
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