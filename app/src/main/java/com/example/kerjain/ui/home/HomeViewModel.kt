package com.example.kerjain.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.AppExecutor
import com.example.kerjain.data.Lowongan

class HomeViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> get() = _categories

    private val _recommended = MutableLiveData<List<Lowongan>>()
    val recommended: LiveData<List<Lowongan>> get() = _recommended

    private var cachedRecommended: List<Lowongan> = emptyList()

    companion object {
        val DEFAULT_CATEGORIES = listOf(
            "Admin",
            "Akuntansi",
            "Sales",
            "Web Developer",
            "Mobile Developer",
            "Guru",
            "Driver",
            "Hotel",
            "Dosen",
            "Pabrik",
            "Digital Marketing",
            "Desain Grafis",
            "Barista",
            "Chef",
            "Perawat",
            "Security",
            "Gudang",
            "Customer Service",
            "System Analyst",
            "Kebersihan",
            "Kesehatan",
            "Konstruksi",
            "Logistik & Operasional",
            "Perbankan",
            "Retail",
            "Teknisi",
            "Translator",
            "Media",
            "Konten Kreator"
        )
    }

    init {
        _categories.value = DEFAULT_CATEGORIES
    }

    fun loadRecommended(db: AppDatabase, executor: AppExecutor, limit: Int = 10) {
        executor.diskIO.execute {

            val list = try {
                db.lowonganDao().getAllSync()
            } catch (e: Exception) {
                emptyList<Lowongan>()
            }

            val recommended = list.take(limit)

            cachedRecommended = recommended

            executor.mainThread.execute {
                _recommended.value = recommended
            }
        }
    }

    fun filterRecommended(query: String) {

        if (query.isBlank()) {
            _recommended.value = cachedRecommended
            return
        }

        val q = query.lowercase().trim()

        val filtered = cachedRecommended.filter { l ->
            l.judul.lowercase().contains(q) ||
                    l.lokasi.lowercase().contains(q) ||
                    l.gaji.lowercase().contains(q)
        }

        _recommended.value = filtered
    }

    fun loadByCategory(db: AppDatabase, executor: AppExecutor, categoryPattern: String) {

        executor.diskIO.execute {

            val list = try {
                db.lowonganDao().getByCategory(categoryPattern)
            } catch (e: Exception) {
                emptyList<Lowongan>()
            }

            cachedRecommended = list

            executor.mainThread.execute {
                _recommended.value = list
            }
        }
    }
}
