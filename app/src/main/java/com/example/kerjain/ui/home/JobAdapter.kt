package com.example.kerjain.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.model.Job
import com.google.android.material.card.MaterialCardView

class JobAdapter(
    private val onJobClick: (Job) -> Unit,
    private val onBookmarkClick: (Job) -> Unit
) : ListAdapter<Job, JobAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardJob: MaterialCardView = view.findViewById(R.id.cardJob)
        val ivCompanyLogo: ImageView = view.findViewById(R.id.ivCompanyLogo)
        val ivBookmark: ImageView = view.findViewById(R.id.ivBookmark)
        val tvJobTitle: TextView = view.findViewById(R.id.tvJobTitle)
        val tvCompanyName: TextView = view.findViewById(R.id.tvCompanyName)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        val tvDistance: TextView = view.findViewById(R.id.tvDistance)
        val tvSalary: TextView = view.findViewById(R.id.tvSalary)
        val tvPostedTime: TextView = view.findViewById(R.id.tvPostedTime)
        val cvSponsored: MaterialCardView = view.findViewById(R.id.cvSponsored)

        fun bind(job: Job) {
            tvJobTitle.text = job.title
            tvCompanyName.text = job.company
            tvLocation.text = job.location
            tvDistance.text = job.distance
            tvSalary.text = job.getSalaryRange()
            tvPostedTime.text = job.postedDate

            // Show sponsored badge
            cvSponsored.visibility = if (job.isSponsored) View.VISIBLE else View.GONE

            // Bookmark icon
            ivBookmark.setImageResource(
                if (job.isBookmarked) R.drawable.ic_bookmark_filled
                else R.drawable.ic_bookmark_outline
            )

            // TODO: Load company logo
            // Glide.with(itemView.context)
            //     .load(job.companyLogo)
            //     .placeholder(R.drawable.ic_company_placeholder)
            //     .into(ivCompanyLogo)

            cardJob.setOnClickListener {
                onJobClick(job)
            }

            ivBookmark.setOnClickListener {
                job.isBookmarked = !job.isBookmarked
                ivBookmark.setImageResource(
                    if (job.isBookmarked) R.drawable.ic_bookmark_filled
                    else R.drawable.ic_bookmark_outline
                )
                onBookmarkClick(job)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem == newItem
        }
    }
}