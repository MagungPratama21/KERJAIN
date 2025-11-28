package com.example.kerjain.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    private lateinit var ivProfilePicture: ImageView
    private lateinit var ivNotification: ImageView
    private lateinit var etSearch: EditText
    private lateinit var ivFilter: ImageView
    private lateinit var cvLocation: View
    private lateinit var tvLocation: TextView
    private lateinit var tvLowonganCount: TextView
    private lateinit var tvLamaranCount: TextView
    private lateinit var rvJobCategories: RecyclerView
    private lateinit var rvJobs: RecyclerView
    private lateinit var tvSeeAll: TextView

    private lateinit var categoryAdapter: JobCategoryAdapter
    private lateinit var jobAdapter: JobAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_pelamar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        initViews(view)
        setupRecyclerViews()
        setupListeners()
        observeViewModel()

        viewModel.loadCategories()
        viewModel.loadJobs()
        viewModel.loadUserStats()
    }

    private fun initViews(view: View) {
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture)
        ivNotification = view.findViewById(R.id.ivNotification)
        etSearch = view.findViewById(R.id.etSearch)
        ivFilter = view.findViewById(R.id.ivFilter)
        cvLocation = view.findViewById(R.id.cvLocation)
        tvLocation = view.findViewById(R.id.tvLocation)
        tvLowonganCount = view.findViewById(R.id.tvLowonganCount)
        tvLamaranCount = view.findViewById(R.id.tvLamaranCount)
        rvJobCategories = view.findViewById(R.id.rvJobCategories)
        rvJobs = view.findViewById(R.id.rvJobs)
        tvSeeAll = view.findViewById(R.id.tvSeeAll)
    }

    private fun setupRecyclerViews() {
        categoryAdapter = JobCategoryAdapter { category ->
            viewModel.filterByCategory(category)
        }
        rvJobCategories.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = categoryAdapter
        }

        jobAdapter = JobAdapter(
            onJobClick = { job ->
                // Navigate to job detail
                // TODO: Navigate to JobDetailActivity
            },
            onBookmarkClick = { job ->
                viewModel.toggleBookmark(job)
            }
        )
        rvJobs.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = jobAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupListeners() {
        ivNotification.setOnClickListener {
            // Navigate to notifications
            // TODO: Navigate to NotificationsActivity
        }

        ivFilter.setOnClickListener {
            // Show filter dialog
            // TODO: Show FilterDialogFragment
        }

        cvLocation.setOnClickListener {
            // Show location picker
            // TODO: Show LocationPickerDialog
        }

        tvSeeAll.setOnClickListener {
            // Navigate to all jobs
            // TODO: Navigate to LowonganFragment or show all
        }

        ivProfilePicture.setOnClickListener {
            // Navigate to profile
            // TODO: Switch to ProfileFragment
        }
    }

    private fun observeViewModel() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.submitList(categories)
        }

        viewModel.jobs.observe(viewLifecycleOwner) { jobs ->
            jobAdapter.submitList(jobs)
        }

        viewModel.savedJobsCount.observe(viewLifecycleOwner) { count ->
            tvLowonganCount.text = count.toString()
        }

        viewModel.applicationsCount.observe(viewLifecycleOwner) { count ->
            tvLamaranCount.text = count.toString()
        }

        viewModel.selectedLocation.observe(viewLifecycleOwner) { location ->
            tvLocation.text = location
        }
    }
}