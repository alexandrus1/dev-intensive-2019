package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_archive.*
import kotlinx.android.synthetic.main.item_chat_group.*
import kotlinx.android.synthetic.main.item_chat_single.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.utils.Utils

class ChatAdapter(val listener: (ChatItem, Int) -> Unit) : RecyclerView.Adapter<ChatAdapter.ChatItemViewHolder>() {
    companion object {
        private const val ARCHIVE_TYPE = 0
        private const val SINGLE_TYPE = 1
        private const val GROUP_TYPE = 2
    }

    var items: List<ChatItem> = listOf()

    override fun getItemViewType(position: Int): Int = when (items[position].chatType) {
        ChatType.ARCHIVE -> ARCHIVE_TYPE
        ChatType.SINGLE -> SINGLE_TYPE
        ChatType.GROUP -> GROUP_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ARCHIVE_TYPE -> ArchiveViewHolder(inflater.inflate(R.layout.item_chat_archive, parent, false))
            SINGLE_TYPE -> SingleViewHolder(inflater.inflate(R.layout.item_chat_single, parent, false))
            else -> GroupViewHolder(inflater.inflate(R.layout.item_chat_group, parent, false))
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        Log.d("M_ChatAdapter", "onBindViewHolder $position")
        holder.bind(items[position], position, listener)
    }

    fun updateData(data: List<ChatItem>) {
        Log.d(
            "M_ChatAdapter", "update data adapter - new data ${data.size} hash: ${data.hashCode()}" +
                    " old data ${items.size} hash: ${items.hashCode()}"
        )

        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].id == data[newPos].id

            // т.к. мы используем data классы, мы можем здесь просто сравнить их хэш-коды
            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].hashCode() == data[newPos].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    abstract inner class ChatItemViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        abstract fun bind(item: ChatItem, pos: Int, listener: (ChatItem, Int) -> Unit)
    }

    inner class SingleViewHolder(convertView: View) : ChatItemViewHolder(convertView),
        ItemTouchViewHolder {
        override fun onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY)
            val color = Utils.getThemeColor(R.attr.colorItemSelected, itemView.context.theme)
            itemView.setBackgroundColor(color)
        }

        override fun onItemCleared() {
            val color = Utils.getThemeColor(R.attr.colorItem, itemView.context.theme)
            itemView.setBackgroundColor(color)
        }

        override fun bind(item: ChatItem, pos: Int, listener: (ChatItem, Int) -> Unit) {
            // Нельзя обращаться к вь
            // ю через itemView! itemView.tv_title_single.text ...
            // убивает производительность, т.к. вьюхи не кэшируются, а ищутся каждый раз заново
            // решается с помощью реализации холдером LayoutContainer
            if (item.avatar == null) {
                // из-за переиспользовании вью холдеров нужно очищать вью,
                // чтобы glide не продолжал грузить в неё изображение
                // glide работает асинхронно и кэширует изображения
                Glide.with(itemView)
                    .clear(iv_avatar_single)
                iv_avatar_single.setDefaultAvatar(item.initials, Color.LTGRAY)
            } else {
                // glide, picasso и фреска - три основных библиотеки для работы с графикой
                // фреску лучше использовать для сложных работ с графикой (2:01:45 в 5 занятии)
                Glide.with(itemView)
                    .load(item.avatar)
                    .into(iv_avatar_single)
            }
            sv_indicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE
            with(tv_date_single) {
                visibility = if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }
            with(tv_counter_single) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            tv_title_single.text = item.title
            tv_message_single.text = item.shortDescription?.trim()

            itemView.setOnClickListener {
                listener.invoke(item, pos)
            }
        }
    }

    inner class GroupViewHolder(convertView: View) : ChatItemViewHolder(convertView),
        ItemTouchViewHolder {
        override fun onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY)
            val color = Utils.getThemeColor(R.attr.colorItemSelected, itemView.context.theme)
            itemView.setBackgroundColor(color)
        }

        override fun onItemCleared() {
            val color = Utils.getThemeColor(R.attr.colorItem, itemView.context.theme)
            itemView.setBackgroundColor(color)
        }

        override fun bind(item: ChatItem, pos: Int, listener: (ChatItem, Int) -> Unit) {

            iv_avatar_group.setDefaultAvatar(item.title[0].toString(), Color.LTGRAY)

            with(tv_date_group) {
                visibility = if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }
            with(tv_counter_group) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            tv_title_group.text = item.title
            tv_message_group.text = item.shortDescription?.trim()

            with(tv_message_author_group) {
                visibility = if (item.author != null) View.VISIBLE else View.GONE
                text = resources.getString(R.string.item_author, item.author)
            }

            itemView.setOnClickListener {
                listener.invoke(item, pos)
            }
        }
    }

    inner class ArchiveViewHolder(convertView: View) : ChatItemViewHolder(convertView) {

        override fun bind(item: ChatItem, pos: Int, listener: (ChatItem, Int) -> Unit) {
            with(tv_date_archive) {
                visibility = if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }
            with(tv_counter_archive) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            tv_message_archive.text = item.shortDescription?.trim()

            with(tv_message_author_archive) {
                visibility = if (item.author != null) View.VISIBLE else View.GONE
                text = resources.getString(R.string.item_author, item.author)
            }

            itemView.setOnClickListener {
                listener.invoke(item, pos)
            }

        }
    }
}