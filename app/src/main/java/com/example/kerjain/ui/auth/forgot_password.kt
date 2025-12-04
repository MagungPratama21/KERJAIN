package com.example.kerjain.ui.auth

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kerjain.R
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.AppExecutor
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnSubmit: MaterialButton

    private lateinit var db: AppDatabase
    private lateinit var executor: AppExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initViews()
        initDatabase()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        etEmail = findViewById(R.id.etEmail)
        btnSubmit = findViewById(R.id.btnSubmit)
    }

    private fun initDatabase() {
        db = AppDatabase.getDatabase(this)
        executor = AppExecutor()
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (validateInput(email)) {
                checkEmail(email)
            }
        }
    }

    private fun validateInput(email: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkEmail(email: String) {
        executor.diskIO.execute {

            val pelamar = db.pelamarDao().getByEmail(email)

            val perusahaan =
                if (pelamar == null) db.perusahaanDao().getByEmail(email) else null

            executor.mainThread.execute {

                when {
                    pelamar != null -> {
                        Toast.makeText(
                            this,
                            "Link reset password telah dikirim ke email pelamar.",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }

                    perusahaan != null -> {
                        Toast.makeText(
                            this,
                            "Link reset password telah dikirim ke email perusahaan.",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }

                    else -> {
                        Toast.makeText(
                            this,
                            "Email tidak terdaftar!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
