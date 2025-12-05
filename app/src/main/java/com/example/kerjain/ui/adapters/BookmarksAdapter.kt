package com.example.kerjain.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.Bookmark

class BookmarksAdapter(
    val items: MutableList<Bookmark>,
    private val onOpen: (Bookmark) -> Unit,
    private val onRemove: (Bookmark) -> Unit
) : RecyclerView.Adapter<BookmarksAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val tvJobTitle: TextView? = view.findViewById(R.id.tvJobTitle)
        private val tvCompany: TextView? = view.findViewById(R.id.tvCompanyName)
        private val ivBookmark: ImageView? = view.findViewById(R.id.ivBookmark)

        fun bind(b: Bookmark) {
            tvJobTitle?.text = b.title
            tvCompany?.text = b.company
            ivBookmark?.setImageResource(R.drawable.bookmark)

            itemView.setOnClickListener { onOpen(b) }
            ivBookmark?.setOnClickListener { onRemove(b) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_jobcard, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list: List<Bookmark>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int): Bookmark {
        val b = items.removeAt(position)
        notifyItemRemoved(position)
        return b
    }
}
