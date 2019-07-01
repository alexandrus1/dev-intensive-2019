package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        var list = fullName?.split(" ")
        val firstName = list?.getOrNull(0)
        val lastName = list?.getOrNull(1)
        return Pair(
            if (firstName == "" || firstName == " ") null else firstName,
            if (lastName == "" || lastName == " ") null else lastName
        )
    }

    fun transliteration(fullName: String, divider: String = " "): String {
        TODO("not implemented")
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}