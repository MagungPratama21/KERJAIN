package com.example.kerjain.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.Chat
import com.example.kerjain.data.Perusahaan
import java.text.SimpleDateFormat
import java.util.*

class ChatListAdapter(
    private var items: List<ChatListItem>,
    private val onClick: (ChatListItem) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.VH>() {

    data class ChatListItem(
        val companyId: Int,
        val company: Perusahaan?,
        val lastChat: Chat
    )

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCompanyLogo: ImageView = itemView.findViewById(R.id.ivCompanyLogo)
        val tvCompanyName: TextView = itemView.findViewById(R.id.tvCompanyName)
        val tvLastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)

        init {
            itemView.setOnClickListener {
                onClick(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        val company = it.company

        holder.tvCompanyName.text = company?.nama_perusahaan ?: "Perusahaan (${it.companyId})"

        holder.tvLastMessage.text = it.lastChat.message

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.tvTime.text = sdf.format(Date(it.lastChat.timestamp))

        if (company?.logo.isNullOrEmpty()) {
            holder.ivCompanyLogo.setImageResource(R.drawable.company_placeholder)
        } else {
            holder.ivCompanyLogo.setImageResource(R.drawable.company_placeholder)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<ChatListItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}
