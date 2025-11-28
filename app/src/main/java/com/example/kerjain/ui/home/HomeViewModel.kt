package com.example.kerjain.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kerjain.data.model.Job
import com.example.kerjain.data.model.JobCategory

class HomeViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<JobCategory>>()
    val categories: LiveData<List<JobCategory>> = _categories

    private val _jobs = MutableLiveData<List<Job>>()
    val jobs: LiveData<List<Job>> = _jobs

    private val _savedJobsCount = MutableLiveData<Int>()
    val savedJobsCount: LiveData<Int> = _savedJobsCount

    private val _applicationsCount = MutableLiveData<Int>()
    val applicationsCount: LiveData<Int> = _applicationsCount

    private val _selectedLocation = MutableLiveData<String>()
    val selectedLocation: LiveData<String> = _selectedLocation

    init {
        _selectedLocation.value = "Cari Provinsi / Kota / Kabupaten"
        _savedJobsCount.value = 0
        _applicationsCount.value = 0
    }

    fun loadCategories() {
        // TODO: Load from repository/API
        val dummyCategories = listOf(
            JobCategory("1", "Admin", "ic_admin"),
            JobCategory("2", "Akuntansi", "ic_accounting"),
            JobCategory("3", "Sales", "ic_sales"),
            JobCategory("4", "Web Developer", "ic_web_dev"),
            JobCategory("5", "Mobile Developer", "ic_mobile_dev"),
            JobCategory("6", "Guru", "ic_teacher"),
            JobCategory("7", "Driver", "ic_driver"),
            JobCategory("8", "Hotel", "ic_hotel"),
            JobCategory("9", "Dosen", "ic_lecturer"),
            JobCategory("10", "Pabrik", "ic_factory"),
            JobCategory("11", "Digital Marketing", "ic_marketing"),
            JobCategory("12", "Desain Grafis", "ic_design"),
            JobCategory("13", "Barista", "ic_barista"),
            JobCategory("14", "Chef", "ic_chef"),
            JobCategory("15", "Perawat", "ic_nurse"),
            JobCategory("16", "Security", "ic_security"),
            JobCategory("17", "Gudang", "ic_warehouse"),
            JobCategory("18", "Customer Service", "ic_cs"),
            JobCategory("19", "System Analyst", "ic_analyst"),
            JobCategory("20", "Kebersihan", "ic_cleaning"),
            JobCategory("21", "Kesehatan", "ic_health"),
            JobCategory("22", "Konstruksi", "ic_construction"),
            JobCategory("23", "Logistik", "ic_logistics"),
            JobCategory("24", "Perbankan", "ic_bank"),
            JobCategory("25", "Retail", "ic_retail"),
            JobCategory("26", "Teknisi", "ic_technician"),
            JobCategory("27", "Translator", "ic_translator"),
            JobCategory("28", "Media", "ic_media"),
            JobCategory("29", "Konten Kreator", "ic_content"),
            JobCategory("30", "Data Analyst", "ic_data"),
            JobCategory("31", "HR", "ic_hr"),
            JobCategory("32", "Freelancer", "ic_freelance")
        )
        _categories.value = dummyCategories.take(8) // Show first 8
    }

    fun loadJobs() {
        // TODO: Load from repository/API
        val dummyJobs = listOf(
            Job(
                id = "1",
                title = "Senior Flutter Developer",
                company = "Tech Innovators Indonesia",
                location = "Jakarta Selatan",
                distance = "2.5 km",
                salaryMin = 15000000,
                salaryMax = 25000000,
                postedDate = "2 hari yang lalu",
                isSponsored = true,
                companyLogo = "logo_tech_innovators"
            ),
            Job(
                id = "2",
                title = "Sales Executive",
                company = "Global Marketing Solutions",
                location = "Tangerang",
                distance = "8.3 km",
                salaryMin = 8000000,
                salaryMax = 15000000,
                postedDate = "1 hari yang lalu",
                isSponsored = true,
                companyLogo = "logo_global_marketing"
            ),
            Job(
                id = "3",
                title = "Accounting Manager",
                company = "PT Finansial Sejahtera",
                location = "Jakarta Pusat",
                distance = "5.1 km",
                salaryMin = 12000000,
                salaryMax = 18000000,
                postedDate = "3 hari yang lalu",
                isSponsored = false,
                companyLogo = "logo_finansial"
            )
        )
        _jobs.value = dummyJobs
    }

    fun loadUserStats() {
        // TODO: Load from repository/API
        _savedJobsCount.value = 0
        _applicationsCount.value = 0
    }

    fun filterByCategory(category: JobCategory) {
        // TODO: Implement category filter
    }

    fun toggleBookmark(job: Job) {
        // TODO: Implement bookmark toggle
    }
}