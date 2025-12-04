package com.example.kerjain.ui.chatroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.Chat
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomAdapter(
    private val currentUserId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_SENT = 1
    private val TYPE_RECEIVED = 2

    private var items: MutableList<Chat> = mutableListOf()

    inner class SentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessageTextSent)
        val tvTime: TextView = itemView.findViewById(R.id.tvMessageTimeSent)
    }

    inner class RecvVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessageText)
        val tvTime: TextView = itemView.findViewById(R.id.tvMessageTime)
    }

    override fun getItemViewType(position: Int): Int {
        val chat = items[position]
        return if (chat.sender_id == currentUserId) TYPE_SENT else TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SENT) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            SentVH(v)
        } else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            RecvVH(v)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = items[position]
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = sdf.format(Date(chat.timestamp))

        if (holder is SentVH) {
            holder.tvMessage.text = chat.message
            holder.tvTime.text = time
        } else if (holder is RecvVH) {
            holder.tvMessage.text = chat.message
            holder.tvTime.text = time
        }
    }

    fun setItems(newItems: List<Chat>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItem(chat: Chat) {
        items.add(chat)
        notifyItemInserted(items.size - 1)
    }
}
