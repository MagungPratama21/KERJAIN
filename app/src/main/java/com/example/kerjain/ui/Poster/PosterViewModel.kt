package com.example.kerjain.ui.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.AppExecutor
import com.example.kerjain.data.Lowongan

class PosterViewModel : ViewModel() {

    private val _posters = MutableLiveData<List<Lowongan>>()
    val posters: LiveData<List<Lowongan>> get() = _posters

    fun loadPosters(db: AppDatabase, executor: AppExecutor) {
        executor.diskIO.execute {
            val list = try {
                db.lowonganDao().getAllSync()
            } catch (e: Exception) {
                emptyList()
            }

            executor.mainThread.execute {
                _posters.value = list
            }
        }
    }

    fun filterPoster(query: String) {
        val current = _posters.value ?: return
        if (query.isBlank()) {
            _posters.value = current
            return
        }

        val q = query.lowercase()
        _posters.value = current.filter {
            it.judul.lowercase().contains(q) || it.lokasi.lowercase().contains(q)
        }
    }
}
