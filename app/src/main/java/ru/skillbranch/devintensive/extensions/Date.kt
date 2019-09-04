package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.absoluteValue

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits {
    SECOND, MINUTE, HOUR, DAY
}

fun Date.shortFormat(): String {
    val pattern = if (this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    return SimpleDateFormat(pattern, Locale("ru")).format(this)
}

private fun Date.isSameDay(otherDate: Date): Boolean = (this.time / DAY) == (otherDate.time / DAY)

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time
    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = (date.getTime() - this.getTime())
    if (diff >= 0) {
        if (diff <= 1 * SECOND) return "только что"
        else if (diff <= 45 * SECOND) return "несколько секунд назад"
        else if (diff <= 75 * SECOND) return "минуту назад"
        else if (diff <= 45 * MINUTE) {
            val value: Long = diff / MINUTE
            val sb = StringBuilder()
            return sb.humanizeDiff(value, "", " назад", "минуту", "минуты", "минут").toString()
        } else if (diff <= 75 * MINUTE) return "час назад"
        else if (diff <= 22 * HOUR) {
            val value: Long = diff / HOUR
            val sb = StringBuilder()
            return sb.humanizeDiff(value, "", " назад", "час", "часа", "часов").toString()
        } else if (diff <= 26 * HOUR) return "день назад"
        else if (diff <= 360 * DAY) {
            val value: Long = diff / DAY
            val sb = StringBuilder()
            return sb.humanizeDiff(value, "", " назад", "день", "дня", "дней").toString()
        } else if (diff > 360 * DAY) return "более года назад"
    } else {                // future
        if (diff >= -1 * SECOND) return "только что"
        else if (diff >= -45 * SECOND) return "через несколько секунд"
        else if (diff >= -75 * SECOND) return "через минуту"
        else if (diff >= -45 * MINUTE) {
            val value: Long = diff / MINUTE
            val sb = StringBuilder()
            return sb.humanizeDiff(value, "через ", "", "минуту", "минуты", "минут").toString()
        } else if (diff >= -75 * MINUTE) return "через час"
        else if (diff >= -22 * HOUR) {
            val value: Long = diff / HOUR
            val sb = StringBuilder()
            return sb.humanizeDiff(value, "через ", "", "час", "часа", "часов").toString()
        } else if (diff >= -26 * HOUR) return "через день"
        else if (diff >= -360 * DAY) {
            val value: Long = diff / DAY
            val sb = StringBuilder()
            return sb.humanizeDiff(value, "через ", "", "день", "дня", "дней").toString()
        } else if (diff < -360 * DAY) return "более чем через год"
    }


    return ""
}

fun StringBuilder.humanizeDiff(
    valueIn: Long,
    textBefore: String,
    textAfter: String,
    text1: String,
    text234: String,
    textOther: String
): StringBuilder {
    val value = valueIn.absoluteValue
    val reminder1 = value % 100
    val reminder = value % 10
    this.append(textBefore)
    this.append(value.toString()).append(" ")

    if (reminder1 >= 11L && reminder1 <= 19L) this.append(textOther)
    else if (reminder == 1L) this.append(text1)
    else if (reminder == 2L || reminder == 3L || reminder == 4L) this.append(text234)
    else this.append(textOther)

    this.append(textAfter)

    return this
}