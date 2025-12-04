package com.example.kerjain.ui.lowongan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.AppExecutor
import com.example.kerjain.data.Lowongan

class LowonganViewModel : ViewModel() {

    private val _jobs = MutableLiveData<List<Lowongan>>()
    val jobs: LiveData<List<Lowongan>> get() = _jobs

    private var cachedJobs: List<Lowongan> = emptyList()

    fun loadAll(db: AppDatabase, executor: AppExecutor) {
        executor.diskIO.execute {
            val data = db.lowonganDao().getAllSync()
            cachedJobs = data

            executor.mainThread.execute {
                _jobs.value = data
            }
        }
    }

    fun loadLatest(db: AppDatabase, executor: AppExecutor) {
        executor.diskIO.execute {
            val data = db.lowonganDao().getAllSync()
                .sortedByDescending { it.tanggal_post }

            cachedJobs = data

            executor.mainThread.execute {
                _jobs.value = data
            }
        }
    }

    fun search(keyword: String) {
        if (keyword.isBlank()) {
            _jobs.value = cachedJobs
            return
        }

        val q = keyword.trim().lowercase()
        val filtered = cachedJobs.filter {
            it.judul.lowercase().contains(q) ||
                    it.lokasi.lowercase().contains(q) ||
                    it.gaji.lowercase().contains(q)
        }

        _jobs.value = filtered
    }

    fun loadSaved() {
        _jobs.value = emptyList() // kosong dulu
    }
}
