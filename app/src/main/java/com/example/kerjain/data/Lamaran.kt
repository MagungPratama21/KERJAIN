package com.example.kerjain.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lamaran")
data class Lamaran(
    @PrimaryKey(autoGenerate = true)
    val lamaran_id: Int = 0,
    val pelamar_id: Int,
    val job_id: Int,
    val status_lamaran: String,
    val tanggal_lamaran: Long
)
