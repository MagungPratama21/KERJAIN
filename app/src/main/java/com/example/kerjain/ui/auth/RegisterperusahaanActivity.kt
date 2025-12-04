package com.example.kerjain.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kerjain.R
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.AppExecutor
import com.example.kerjain.data.Perusahaan

class RegisterperusahaanActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var imgLogo: ImageView
    private lateinit var tvUploadLogo: TextView

    private lateinit var etCompanyName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    private lateinit var db: AppDatabase
    private lateinit var executor: AppExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerperusahaan)

        initViews()
        initDatabase()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        imgLogo = findViewById(R.id.imgLogo)
        tvUploadLogo = findViewById(R.id.tvUploadLogo)

        etCompanyName = findViewById(R.id.etCompanyName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
    }

    private fun initDatabase() {
        db = AppDatabase.getDatabase(this)
        executor = AppExecutor()
    }

    private fun setupListeners() {

        btnBack.setOnClickListener { finish() }

        tvUploadLogo.setOnClickListener {
            Toast.makeText(this, "Upload logo belum diimplementasikan", Toast.LENGTH_SHORT).show()
        }

        btnRegister.setOnClickListener {
            val name = etCompanyName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirm = etConfirmPassword.text.toString().trim()

            if (!validateInput(name, email, phone, address, password, confirm)) return@setOnClickListener

            registerCompany(name, email, phone, address, password)
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        phone: String,
        address: String,
        password: String,
        confirm: String
    ): Boolean {

        if (name.isEmpty()) {
            Toast.makeText(this, "Nama perusahaan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "Nomor telepon tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (address.isEmpty()) {
            Toast.makeText(this, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 12) {
            Toast.makeText(this, "Password minimal 12 karakter", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirm) {
            Toast.makeText(this, "Konfirmasi password tidak sama", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerCompany(
        name: String,
        email: String,
        phone: String,
        address: String,
        password: String
    ) {

        val perusahaan = Perusahaan(
            perusahaan_id = 0,
            nama_perusahaan = name,
            email = email,
            password = password,
            deskripsi = null,
            alamat = address,
            logo = null
        )

        executor.diskIO.execute {

            val existing = db.perusahaanDao().getByEmail(email)
            if (existing != null) {
                executor.mainThread.execute {
                    Toast.makeText(
                        this,
                        "Email sudah digunakan!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@execute
            }

            db.perusahaanDao().insert(perusahaan)

            executor.mainThread.execute {
                Toast.makeText(this, "Registrasi perusahaan berhasil!", Toast.LENGTH_LONG).show()

                val intent = Intent(this, LoginperusahaanActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }
    }
}
