package ru.skillbranch.devintensive.models

import java.util.*

import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.models.data.*

class ImageMessage(
    id: String,
    from: User,
    chat: Chat,
    isIncoming: Boolean = false,
    date: Date = Date(),
    isReaded:Boolean = false,
    var image: String?
) : BaseMessage(id, from, chat, isIncoming, date, isReaded) {

    override fun shortMessage(): String = "${from.firstName} - �������� ����"

    override fun formatMessage(): String =
        "id:$id ${from.firstName} ${if (isIncoming) "�������" else "��������"} ����������� \"$image\" ${date.humanizeDiff()}"
}