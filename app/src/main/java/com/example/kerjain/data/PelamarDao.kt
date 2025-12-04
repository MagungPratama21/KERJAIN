package com.example.kerjain.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PelamarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pelamar: Pelamar)

    @Query("SELECT * FROM pelamar WHERE email = :email AND password = :password LIMIT 1")
     fun login(email: String, password: String): Pelamar?

    @Query("SELECT * FROM pelamar ORDER BY pelamar_id ASC")
    fun getAllLive(): LiveData<List<Pelamar>>

    @Query("SELECT * FROM pelamar ORDER BY pelamar_id ASC")
    fun getAllSync(): List<Pelamar>

    @Query("SELECT * FROM pelamar WHERE email = :email LIMIT 1")
    fun getByEmail(email: String): Pelamar?


}
