package com.example.kerjain

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class TipePekerjaanActivity : AppCompatActivity() {

    private lateinit var btnPelamar: MaterialButton
    private lateinit var btnPerusahaan: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipe_pekerjaan)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        btnPelamar = findViewById(R.id.btnpelamar)
        btnPerusahaan = findViewById(R.id.btnperusahaan)
    }

    private fun setupListeners() {
        btnPelamar.setOnClickListener {
            val intent = Intent(this, LoginPelamarActivity::class.java)
            startActivity(intent)
        }

        btnPerusahaan.setOnClickListener {
            val intent = Intent(this, LoginperusahaanActivity::class.java)
            startActivity(intent)
        }
    }
}