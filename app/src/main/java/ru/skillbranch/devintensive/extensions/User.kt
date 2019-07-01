package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.*
import ru.skillbranch.devintensive.utils.*

fun User.toUserView(): UserView {
    val nickName = Utils.transliteration("$firstName $lastName")
    val initials = Utils.toInitials(firstName, lastName)
    val status =
        if (lastVisit == null) "Еще ни разу не был"
        else if (isOnline) "online"
        else "Последний раз был ${lastVisit.humanizeDiff()}"

    return UserView(
        id,
        fullName = "$firstName $lastName",
        nickName = nickName,
        initials = initials,
        avatar = avatar,
        status = status
    )
}

