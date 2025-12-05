package com.example.kerjain.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.NotificationItem
import java.text.DateFormat
import java.util.*

class NotificationsAdapter(
    val items: MutableList<NotificationItem>,
    private val onRemove: (NotificationItem) -> Unit
) : RecyclerView.Adapter<NotificationsAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val tTitle: TextView = view.findViewById(R.id.tvNotifTitle)
        private val tBody: TextView = view.findViewById(R.id.tvNotifBody)
        private val tTime: TextView = view.findViewById(R.id.tvNotifTime)
        private val btnRemove: ImageView = view.findViewById(R.id.btnRemoveNotif)

        fun bind(n: NotificationItem) {
            tTitle.text = n.title
            tBody.text = n.body
            tTime.text = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(n.createdAt))
            btnRemove.setOnClickListener { onRemove(n) }
            itemView.setOnClickListener { /* optional: open detail */ }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

    fun submitList(list: List<NotificationItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int): NotificationItem {
        val it = items.removeAt(position)
        notifyItemRemoved(position)
        return it
    }
}
