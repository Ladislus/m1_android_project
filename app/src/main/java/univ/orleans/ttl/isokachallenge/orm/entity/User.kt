package univ.orleans.ttl.isokachallenge.orm.entity

import org.springframework.security.crypto.bcrypt.BCrypt
import java.time.LocalDateTime

class User(internal var username: String, password: String, salt: String?, date: LocalDateTime) {

    internal var salt: String = salt ?: BCrypt.gensalt(12)
    internal var password: String = if (salt == null) BCrypt.hashpw(password, this.salt)  else password

    private val _date: LocalDateTime = date
    internal val date : String get() = this._date.toString()

    constructor(username: String, password: String, date: LocalDateTime) : this(username, password, null, date)
}