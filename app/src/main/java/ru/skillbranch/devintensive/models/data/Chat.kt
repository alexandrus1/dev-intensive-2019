package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int = messages.count { !it.isReaded }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? {
        return if (messages.isEmpty()) null else messages.last().date
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String, String?> {
        if (messages.isEmpty()) return "Сообщений ещё нет" to null
        val lastMessage = messages.last()
        return lastMessage.shortMessage() to "${lastMessage.from.firstName}"
    }

    private fun isSingle(): Boolean = members.size == 1

    companion object {
        private const val ARCHIVE_ID = "-1"
        fun toArchiveChatItem(chats: List<Chat>): ChatItem? {
            return if (chats.isEmpty()) null
            else {
                val lastChat = chats.sortedByDescending { it.lastMessageDate() }.first()
                val (message, author) = lastChat.lastMessageShort()
                ChatItem(
                    ARCHIVE_ID,
                    null,
                    "",
                    "Архив чатов",
                    message,
                    chats.sumBy { it.unreadableMessageCount() },
                    lastChat.lastMessageDate()?.shortFormat(),
                    false,
                    ChatType.ARCHIVE,
                    author
                )
            }
        }
    }

    fun toChatItem(): ChatItem  = when {
        isSingle() -> {
            val user = members.first()
            ChatItem(
                id = id,
                avatar = user.avatar,
                initials = Utils.toInitials(user.firstName, user.lastName) ?: "??",
                title = "${user.firstName ?: ""} ${user.lastName ?: ""}",
                shortDescription = lastMessageShort().first,
                messageCount = unreadableMessageCount(),
                lastMessageDate = lastMessageDate()?.shortFormat(),
                isOnline = user.isOnline
            )
        }
        else-> ChatItem(
                id = id,
                avatar = null,
                initials = "",
                title = title,
                shortDescription = lastMessageShort().first,
                messageCount = unreadableMessageCount(),
                lastMessageDate = lastMessageDate()?.shortFormat(),
                isOnline = false,
                chatType = ChatType.GROUP,
                author = lastMessageShort().second
            )
    }
}

enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}
