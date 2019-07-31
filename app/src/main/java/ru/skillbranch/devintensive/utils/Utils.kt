package ru.skillbranch.devintensive.utils

import java.lang.StringBuilder
import android.content.Context
import android.util.TypedValue

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
        fullName.replace(" ", divider)
        var string = StringBuilder()
        fullName.forEach {
            var char = when(it.toString().toLowerCase()) {
                "а"-> "a"
                "б"-> "b"
                "в"-> "v"
                "г"-> "g"
                "д"-> "d"
                "е"-> "e"
                "ё"-> "e"
                "ж"-> "zh"
                "з"-> "z"
                "и"-> "i"
                "й"-> "i"
                "к"-> "k"
                "л"-> "l"
                "м"-> "m"
                "н"-> "n"
                "о"-> "o"
                "п"-> "p"
                "р"-> "r"
                "с"-> "s"
                "т"-> "t"
                "у"-> "u"
                "ф"-> "f"
                "х"-> "h"
                "ц"-> "c"
                "ч"-> "ch"
                "ш"-> "sh"
                "щ"-> "sh'"
                "ъ"-> ""
                "ы"-> "i"
                "ь"-> ""
                "э"-> "e"
                "ю"-> "yu"
                "я"-> "ya"
                " "-> divider
                else -> it
            }
            if (it.isUpperCase())
                char = char.toString().capitalize()
            string.append(char.toString())
        }
        return string.toString()
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        var char1 = firstName?.trim()?.getOrNull(0)?.toTitleCase()
        var char2 = lastName?.trim()?.getOrNull(0)?.toTitleCase()

        var sb = StringBuilder();
        if (char1 != null) sb.append(char1)
        if (char2 != null) sb.append(char2)

        var result : String? = sb.toString()
        if (result == "")
            result = null

        return result
    }

    fun convertPxToDp(context: Context, px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    fun convertDpToPx(context: Context, dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun convertSpToPx(context: Context, sp: Int): Int {
        return sp * context.resources.displayMetrics.scaledDensity.toInt()
    }

    fun convertSpToPx(context: Context, sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)
    }
}