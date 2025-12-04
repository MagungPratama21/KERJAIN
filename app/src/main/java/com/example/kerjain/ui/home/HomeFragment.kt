package com.example.kerjain.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.AppExecutor
import com.example.kerjain.data.Lowongan
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.databinding.FragmentHomePelamarBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomePelamarBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private lateinit var executor: AppExecutor
    private lateinit var viewModel: HomeViewModel

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var jobAdapter: JobAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomePelamarBinding.inflate(inflater, container, false)
        val root = binding.root

        db = AppDatabase.getDatabase(requireContext())
        executor = AppExecutor()
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]


        categoryAdapter = CategoryAdapter(HomeViewModel.DEFAULT_CATEGORIES) { category ->
            viewModel.loadByCategory(db, executor, "%$category%")
        }

        binding.rvJobCategories.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            adapter = categoryAdapter
        }

        jobAdapter = JobAdapter(mutableListOf()) { lowongan ->
        }

        binding.rvRecommendedJobs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobAdapter
        }

        viewModel.recommended.observe(viewLifecycleOwner) { list ->
            jobAdapter.setItems(list)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.filterRecommended(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.loadRecommended(db, executor)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    class CategoryAdapter(
        private val items: List<String>,
        private val onClick: (String) -> Unit
    ) : RecyclerView.Adapter<CategoryAdapter.VH>() {

        inner class VH(val binding: com.example.kerjain.databinding.ItemJobKategoryBinding)
            : RecyclerView.ViewHolder(binding.root) {

            init {
                binding.root.setOnClickListener {
                    binding.cbCategory.isChecked = !binding.cbCategory.isChecked
                    onClick(binding.cbCategory.text.toString())
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val inflater = LayoutInflater.from(parent.context)
            val binding = com.example.kerjain.databinding.ItemJobKategoryBinding.inflate(
                inflater, parent, false
            )
            return VH(binding)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.binding.cbCategory.text = items[position]
        }

        override fun getItemCount(): Int = items.size
    }


    class JobAdapter(
        private val items: MutableList<Lowongan>,
        private val onClick: (Lowongan) -> Unit
    ) : androidx.recyclerview.widget.RecyclerView.Adapter<JobAdapter.VH>() {

        inner class VH(val binding: com.example.kerjain.databinding.ItemJobcardBinding)
            : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

            init {
                binding.root.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        onClick(items[pos])
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val inflater = LayoutInflater.from(parent.context)
            val binding = com.example.kerjain.databinding.ItemJobcardBinding.inflate(
                inflater, parent, false
            )
            return VH(binding)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val job = items[position]
            holder.binding.tvJobTitle.text = job.judul
            holder.binding.tvCompanyName.text = "Perusahaan #${job.perusahaan_id}"
            holder.binding.tvLocation.text = job.lokasi
            holder.binding.tvSalary.text = job.gaji
        }

        override fun getItemCount(): Int = items.size

        fun setItems(newItems: List<Lowongan>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
    }
}
