package com.example.kerjain.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PerusahaanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(perusahaan: Perusahaan)

    @Query("SELECT * FROM perusahaan WHERE email = :email AND password = :password LIMIT 1")
    fun login(email: String, password: String): Perusahaan?

    @Query("SELECT * FROM perusahaan ORDER BY perusahaan_id ASC")
    fun getAllLive(): LiveData<List<Perusahaan>>

    @Query("SELECT * FROM perusahaan ORDER BY perusahaan_id ASC")
    fun getAllSync(): List<Perusahaan>

    @Query("SELECT * FROM perusahaan WHERE perusahaan_id = :id LIMIT 1")
    fun getById(id: Int): Perusahaan?

    @Query("SELECT * FROM perusahaan WHERE email = :email LIMIT 1")
    fun getByEmail(email: String): Perusahaan?


}
