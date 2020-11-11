package univ.orleans.ttl.isokachallenge.orm.entity

import java.time.LocalDateTime

class Drawing(internal var id: Int?, internal var link: String, date: LocalDateTime) {
    private val _date: LocalDateTime = date
    internal val date: String get() = this._date.toString()

    constructor(link: String, date: LocalDateTime) : this(null, link, date)
}