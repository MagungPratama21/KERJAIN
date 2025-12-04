package com.example.kerjain.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lowongan")
data class Lowongan(
    @PrimaryKey(autoGenerate = true)
    val job_id: Int = 0,

    val perusahaan_id: Int,
    val judul: String,
    val tipe: String,
    val lokasi: String,
    val gaji: String,
    val deskripsi: String,
    val tanggal_post: Long
)
