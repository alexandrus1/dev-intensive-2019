package ru.skillbranch.devintensive.models

import java.util.*

import ru.skillbranch.devintensive.utils.*

data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = null,
    val isOnline: Boolean = false
) {
    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    constructor(id: String) : this(id, firstName = "Johnny", lastName = "English")

    init {
        println("It's Alive!!")
    }

    fun printMe(): Unit {
        print(
            """
            id: $id
            firstName: $firstName
            lastName: $lastName
            avatar: $avatar
            rating: $rating
            respect: $respect
            lastVisit: $lastVisit
            isOnline: $isOnline
                    """.trimIndent()
        )
    }

    // factory - generate User object
    companion object Factory {
        private var lastId: Int = -1
        fun makeUser(fullName: String?): User {
            lastId++

            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User(lastId.toString(), firstName, lastName)
        }
    }
}