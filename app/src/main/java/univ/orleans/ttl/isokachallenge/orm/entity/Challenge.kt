package univ.orleans.ttl.isokachallenge.orm.entity

import java.time.LocalDateTime

class Challenge(internal var id: Int?, internal var name: String, internal val type: Boolean, internal var theme: String, date: LocalDateTime, internal var timer: Int = 0) {
    private val _date: LocalDateTime = date
    internal val date: String get() = this._date.toString()

    constructor(name: String, type: Boolean, theme: String, date: LocalDateTime, timer: Int) : this(null, name, type, theme, date)
}