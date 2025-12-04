package com.example.kerjain.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pelamar")
data class Pelamar(
    @PrimaryKey(autoGenerate = true)
    val pelamar_id: Int = 0,
    val nama: String,
    val email: String,
    val password: String,
    val alamat: String? = null,
    val skill: String? = null,
    val pengalaman: String? = null,
    val foto: String? = null
)
