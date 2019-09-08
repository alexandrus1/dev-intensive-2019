package ru.skillbranch.devintensive.models

import java.util.*
import kotlin.math.min

import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.models.data.*

class TextMessage(
    id: String,
    from: User,
    chat: Chat,
    isIncoming: Boolean = false,
    date: Date = Date(),
    isReaded:Boolean = false,
    var text:String?
) : BaseMessage(id, from, chat, isIncoming, date, isReaded) {

    companion object {
        private const val MAX_LENGTH_OF_SHORT_MESSAGE = 128
    }

    override fun shortMessage(): String =
        with(text) {
            if (this.isNullOrEmpty()) ""
            else this.substring(0, min(this.length, MAX_LENGTH_OF_SHORT_MESSAGE))
        }


    override fun formatMessage(): String =
        "id:$id ${from.firstName} ${if (isIncoming) "�������" else "��������"} ��������� \"$text\" ${date.humanizeDiff()}"
}