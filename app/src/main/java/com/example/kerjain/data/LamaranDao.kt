package com.example.kerjain.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LamaranDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lamaran: Lamaran)

    @Query("SELECT * FROM lamaran WHERE pelamar_id = :pelamarId ORDER BY lamaran_id DESC")
    fun getLamaranPelamar(pelamarId: Int): LiveData<List<Lamaran>>

    @Query("SELECT * FROM lamaran WHERE job_id = :jobId ORDER BY lamaran_id DESC")
    fun getPelamarUntukLowongan(jobId: Int): LiveData<List<Lamaran>>
}
