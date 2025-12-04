package com.example.kerjain.ui.lowongan

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.AppExecutor
import com.example.kerjain.data.Lowongan

class LowonganFragment : Fragment() {

    private lateinit var etSearchJob: EditText
    private lateinit var chipAll: TextView
    private lateinit var chipLatest: TextView
    private lateinit var chipSaved: TextView
    private lateinit var rvJobs: RecyclerView
    private lateinit var tvJobCount: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var db: AppDatabase
    private lateinit var executor: AppExecutor
    private lateinit var viewModel: LowonganViewModel

    private lateinit var jobAdapter: JobAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_lowongan_pelamar, container, false)

        etSearchJob = root.findViewById(R.id.etSearchJob)
        chipAll = root.findViewById(R.id.chipAll)
        chipLatest = root.findViewById(R.id.chipLatest)
        chipSaved = root.findViewById(R.id.chipSaved)
        rvJobs = root.findViewById(R.id.rvJobs)
        tvJobCount = root.findViewById(R.id.tvJobCount)
        progressBar = root.findViewById(R.id.progressBar)

        db = AppDatabase.getDatabase(requireContext())
        executor = AppExecutor()
        viewModel = ViewModelProvider(this)[LowonganViewModel::class.java]

        jobAdapter = JobAdapter(mutableListOf()) { lowongan ->
            openJobDetailSafe(lowongan)
        }

        rvJobs.layoutManager = LinearLayoutManager(requireContext())
        rvJobs.adapter = jobAdapter

        viewModel.jobs.observe(viewLifecycleOwner) { list ->
            jobAdapter.setItems(list)
            tvJobCount.text = "Menampilkan ${list.size} lowongan"
            progressBar.visibility = View.GONE
        }

        progressBar.visibility = View.VISIBLE
        viewModel.loadAll(db, executor)

        etSearchJob.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.search(s?.toString() ?: "")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        chipAll.setOnClickListener {
            selectChip(1)
            progressBar.visibility = View.VISIBLE
            viewModel.loadAll(db, executor)
        }

        chipLatest.setOnClickListener {
            selectChip(2)
            progressBar.visibility = View.VISIBLE
            viewModel.loadLatest(db, executor)
        }

        chipSaved.setOnClickListener {
            selectChip(3)
            viewModel.loadSaved()
        }

        return root
    }

    private fun openJobDetailSafe(job: Lowongan) {
        try {
            val intent = Intent().setClassName(requireContext(), "com.example.kerjain.ui.job.JobDetailActivity")
            intent.putExtra("LOWONGAN_ID", job.job_id)
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            } else {
                 Toast.makeText(requireContext(), "JobDetailActivity belum tersedia", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
        }
    }

    private fun selectChip(selected: Int) {

        val selectedDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chip_selected)
        val defaultDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chip_default)

        chipAll.background = if (selected == 1) selectedDrawable else defaultDrawable
        chipLatest.background = if (selected == 2) selectedDrawable else defaultDrawable
        chipSaved.background = if (selected == 3) selectedDrawable else defaultDrawable

        val blue = ContextCompat.getColor(requireContext(), R.color.blue_dark)
        val gray = ContextCompat.getColor(requireContext(), R.color.gray_text)

        chipAll.setTextColor(if (selected == 1) blue else gray)
        chipLatest.setTextColor(if (selected == 2) blue else gray)
        chipSaved.setTextColor(if (selected == 3) blue else gray)
    }


    class JobAdapter(
        private val items: MutableList<Lowongan>,
        private val onClick: (Lowongan) -> Unit
    ) : RecyclerView.Adapter<JobAdapter.VH>() {

        inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle: TextView? = itemView.findViewById(R.id.tvJobTitle)
            val tvCompany: TextView? = itemView.findViewById(R.id.tvCompanyName)
            val tvLocation: TextView? = itemView.findViewById(R.id.tvLocation)
            val tvSalary: TextView? = itemView.findViewById(R.id.tvSalary)

            init {
                itemView.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        onClick(items[pos])
                    }
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
            holder.tvTitle?.text = job.judul
            holder.tvCompany?.text = "Perusahaan #${job.perusahaan_id}"
            holder.tvLocation?.text = job.lokasi
            holder.tvSalary?.text = job.gaji
        }

        override fun getItemCount(): Int = items.size

        fun setItems(newItems: List<Lowongan>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
    }
}
