package univ.orleans.ttl.isokachallenge.orm.entity

class Participation(internal val user: User, internal val drawing: Drawing, internal val challenge: Challenge, internal val is_creator: Boolean, internal var votes: Int = 0)