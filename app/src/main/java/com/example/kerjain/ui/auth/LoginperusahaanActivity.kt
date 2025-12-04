package com.example.kerjain.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kerjain.MainActivity
import com.example.kerjain.R
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.AppExecutor
import com.example.kerjain.data.Perusahaan

class LoginperusahaanActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvForgotPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    private lateinit var db: AppDatabase
    private lateinit var executor: AppExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginperusahaan)

        initViews()
        initDatabase()
        setupListeners()
    }

    private fun initViews() {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
    }

    private fun initDatabase() {
        db = AppDatabase.getDatabase(this)
        executor = AppExecutor()
    }

    private fun setupListeners() {

        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterperusahaanActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (!validateInput(username, password)) return@setOnClickListener

            executor.diskIO.execute {
                val perusahaan = db.perusahaanDao().getByEmail(username)

                executor.mainThread.execute {

                    if (perusahaan == null) {
                        Toast.makeText(this, "Akun perusahaan tidak ditemukan", Toast.LENGTH_SHORT).show()
                        return@execute
                    }

                    if (perusahaan.password != password) {
                        Toast.makeText(this, "Password salah", Toast.LENGTH_SHORT).show()
                        return@execute
                    }

                    Toast.makeText(
                        this,
                        "Login berhasil. Selamat datang, ${perusahaan.nama_perusahaan}",
                        Toast.LENGTH_LONG
                    ).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("COMPANY_ID", perusahaan.perusahaan_id)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            Toast.makeText(this, "Email/username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
