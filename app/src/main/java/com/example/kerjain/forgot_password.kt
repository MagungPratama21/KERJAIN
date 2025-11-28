package com.example.kerjain

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnSubmit: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        etEmail = findViewById(R.id.etEmail)
        btnSubmit = findViewById(R.id.btnSubmit)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString()

            if (validateInput(email)) {
                sendResetLink(email)
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

    private fun sendResetLink(email: String) {
        Toast.makeText(
            this,
            "Link reset password telah dikirim ke $email",
            Toast.LENGTH_LONG
        ).show()
        finish()
    }
}