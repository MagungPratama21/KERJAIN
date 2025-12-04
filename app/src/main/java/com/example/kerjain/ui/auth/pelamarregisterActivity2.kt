package com.example.kerjain.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.*

class pelamarregisterActivity2 : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var rvJobCategories: RecyclerView
    private lateinit var tvSelectedCount: TextView
    private lateinit var btnFinish: Button

    private lateinit var db: AppDatabase
    private lateinit var executor: AppExecutor

    private var regUsername: String? = null
    private var regEmail: String? = null
    private var regPhone: String? = null
    private var regPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelamarregister2)

        initViews()
        readExtras()
        initDatabase()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        rvJobCategories = findViewById(R.id.rvJobCategories)
        tvSelectedCount = findViewById(R.id.tvSelectedCount)
        btnFinish = findViewById(R.id.btnFinish)
    }

    private fun readExtras() {
        regUsername = intent.getStringExtra("reg_username")
        regEmail = intent.getStringExtra("reg_email")
        regPhone = intent.getStringExtra("reg_phone")
        regPassword = intent.getStringExtra("reg_password")
    }

    private fun initDatabase() {
        db = AppDatabase.getDatabase(this)
        executor = AppExecutor()
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnFinish.setOnClickListener {
            savePelamarToDB()
        }
    }

    private fun savePelamarToDB() {

        if (regUsername == null || regEmail == null || regPassword == null) {
            Toast.makeText(this, "Data tidak lengkap", Toast.LENGTH_SHORT).show()
            return
        }

        val pelamar = Pelamar(
            pelamar_id = 0,
            nama = regUsername!!,
            email = regEmail!!,
            password = regPassword!!,
            alamat = null,
            skill = null,
            pengalaman = null,
            foto = null
        )

        executor.diskIO.execute {
            db.pelamarDao().insert(pelamar)

            executor.mainThread.execute {
                Toast.makeText(this, "Pendaftaran berhasil! Silakan login.", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginPelamarActivity::class.java))
                finishAffinity()
            }
        }
    }
}
