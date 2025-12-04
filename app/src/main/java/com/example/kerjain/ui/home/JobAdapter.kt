package com.example.kerjain.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.Lowongan

class JobAdapter(
    private val items: MutableList<Lowongan>,
    private val onClick: (Lowongan) -> Unit
) : RecyclerView.Adapter<JobAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCompanyLogo: ImageView = itemView.findViewById(R.id.ivCompanyLogo)
        val tvJobTitle: TextView = itemView.findViewById(R.id.tvJobTitle)
        val tvCompanyName: TextView = itemView.findViewById(R.id.tvCompanyName)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        val tvSalary: TextView = itemView.findViewById(R.id.tvSalary)
        val tvPostedTime: TextView = itemView.findViewById(R.id.tvPostedTime)
        val ivBookmark: ImageView = itemView.findViewById(R.id.ivBookmark)

        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) onClick(items[pos])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jobcard, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val job = items[position]
        holder.tvJobTitle.text = job.judul
        holder.tvCompanyName.text = "Perusahaan #${job.perusahaan_id}"
        holder.tvLocation.text = job.lokasi
        holder.tvSalary.text = job.gaji
        holder.tvDistance.visibility = View.GONE

        try {
            if (job.tanggal_post > 0L) {
                val diff = System.currentTimeMillis() - job.tanggal_post
                val minutes = diff / (1000 * 60)
                val display = when {
                    minutes < 60 -> "${minutes} menit yang lalu"
                    minutes < 60 * 24 -> "${minutes / 60} jam yang lalu"
                    else -> "${minutes / (60 * 24)} hari yang lalu"
                }
                holder.tvPostedTime.text = display
            } else {
                holder.tvPostedTime.text = ""
            }
        } catch (e: Exception) {
            holder.tvPostedTime.text = ""
        }

        holder.ivCompanyLogo.setImageResource(R.drawable.company)

        holder.ivBookmark.setOnClickListener {
            it.isSelected = !it.isSelected
        }
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<Lowongan>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<Lowongan>) {
        val start = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(start, newItems.size)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}
