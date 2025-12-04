package com.example.kerjain.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perusahaan")
data class Perusahaan(
    @PrimaryKey(autoGenerate = true)
    val perusahaan_id: Int = 0,

    val nama_perusahaan: String,
    val email: String,
    val password: String,

    val deskripsi: String? = null,
    val alamat: String? = null,
    val logo: String? = null
)
