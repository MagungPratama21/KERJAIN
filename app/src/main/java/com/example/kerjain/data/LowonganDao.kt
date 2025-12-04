package com.example.kerjain.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LowonganDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lowongan: Lowongan)

    @Update
    fun update(lowongan: Lowongan)

    @Delete
    fun delete(lowongan: Lowongan)

    @Query("SELECT * FROM lowongan ORDER BY job_id DESC")
    fun getAllLive(): LiveData<List<Lowongan>>

    @Query("SELECT * FROM lowongan ORDER BY job_id DESC")
    fun getAllSync(): List<Lowongan>

    @Query("SELECT * FROM lowongan WHERE perusahaan_id = :companyId ORDER BY job_id DESC")
    fun getByPerusahaan(companyId: Int): LiveData<List<Lowongan>>

    @Query("SELECT * FROM lowongan WHERE job_id = :id LIMIT 1")
    fun getById(id: Int): Lowongan?

    @Query("SELECT * FROM lowongan WHERE category LIKE :category ORDER BY job_id DESC")
    fun getByCategory(category: String): List<Lowongan>
}
