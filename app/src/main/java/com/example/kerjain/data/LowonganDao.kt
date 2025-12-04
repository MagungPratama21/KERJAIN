package com.example.kerjain.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LowonganDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lowongan: Lowongan)

    @Update
    suspend fun update(lowongan: Lowongan)

    @Delete
    suspend fun delete(lowongan: Lowongan)

    @Query("SELECT * FROM lowongan ORDER BY job_id DESC")
    fun getAllLive(): LiveData<List<Lowongan>>

    @Query("SELECT * FROM lowongan ORDER BY job_id DESC")
    suspend fun getAllSync(): List<Lowongan>

    @Query("SELECT * FROM lowongan WHERE perusahaan_id = :companyId ORDER BY job_id DESC")
    fun getByPerusahaan(companyId: Int): LiveData<List<Lowongan>>
}
