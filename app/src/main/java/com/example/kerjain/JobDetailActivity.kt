package com.example.kerjain

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kerjain.data.*

class JobDetailActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var executor: AppExecutor

    private var lowonganId: Int = -1
    private var pelamarId: Int = -1

    private lateinit var btnBack: ImageView
    private lateinit var btnShare: ImageView
    private lateinit var btnBookmark: ImageView
    private lateinit var btnApply: Button

    private lateinit var ivCompanyLogo: ImageView
    private lateinit var tvJobTitle: TextView
    private lateinit var tvCompanyName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvJobType: TextView
    private lateinit var tvSalary: TextView
    private lateinit var tvPostedDate: TextView
    private lateinit var tvJobDescription: TextView
    private lateinit var tvRequirements: TextView
    private lateinit var tvExperience: TextView
    private lateinit var tvCompanyAddress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        db = AppDatabase.getDatabase(this)
        executor = AppExecutor()

        lowonganId = intent.getIntExtra("LOWONGAN_ID", -1)
        pelamarId = intent.getIntExtra("PELAMAR_ID", -1)

        bindViews()
        loadJobDetail()
        setupListeners()
    }

    private fun bindViews() {
        btnBack = findViewById(R.id.btnBack)
        btnShare = findViewById(R.id.btnShare)
        btnBookmark = findViewById(R.id.btnBookmark)
        btnApply = findViewById(R.id.btnApply)

        ivCompanyLogo = findViewById(R.id.ivCompanyLogo)
        tvJobTitle = findViewById(R.id.tvJobTitle)
        tvCompanyName = findViewById(R.id.tvCompanyName)
        tvLocation = findViewById(R.id.tvLocation)
        tvJobType = findViewById(R.id.tvJobType)
        tvSalary = findViewById(R.id.tvSalary)
        tvPostedDate = findViewById(R.id.tvPostedDate)
        tvJobDescription = findViewById(R.id.tvJobDescription)
        tvRequirements = findViewById(R.id.tvRequirements)
        tvExperience = findViewById(R.id.tvExperience)
        tvCompanyAddress = findViewById(R.id.tvCompanyAddress)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }

        btnShare.setOnClickListener {
            Toast.makeText(this, "Fitur Share belum tersedia", Toast.LENGTH_SHORT).show()
        }

        btnBookmark.setOnClickListener {
            Toast.makeText(this, "Disimpan (belum diimplementasi)", Toast.LENGTH_SHORT).show()
        }

        btnApply.setOnClickListener {
            applyToJob()
        }
    }

    private fun loadJobDetail() {
        if (lowonganId == -1) {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        executor.diskIO.execute {
            val lowongan = db.lowonganDao().getById(lowonganId)

            if (lowongan == null) {
                runOnUiThread {
                    Toast.makeText(this, "Lowongan tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return@execute
            }

            val perusahaan = db.perusahaanDao().getById(lowongan.perusahaan_id)

            runOnUiThread {
                tvJobTitle.text = lowongan.judul
                tvCompanyName.text = perusahaan?.nama_perusahaan ?: "Perusahaan #${lowongan.perusahaan_id}"
                tvLocation.text = lowongan.lokasi
                tvJobType.text = lowongan.tipe
                tvSalary.text = lowongan.gaji

                tvPostedDate.text = "Diposting ${calculateDaysAgo(lowongan.tanggal_post)} hari yang lalu"
                tvJobDescription.text = lowongan.deskripsi

                tvRequirements.text = "• Disiplin\n• Bertanggung jawab\n• Komunikatif\n• Detail oriented"
                tvExperience.text = "Minimal 1 tahun"

                tvCompanyAddress.text = perusahaan?.alamat ?: "Alamat tidak tersedia"

                ivCompanyLogo.setImageResource(R.drawable.company_placeholder)
            }
        }
    }

    private fun calculateDaysAgo(timestamp: Long): Int {
        val diff = System.currentTimeMillis() - timestamp
        return (diff / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
    }

    private fun applyToJob() {
        if (pelamarId == -1) {
            Toast.makeText(this, "Akun pelamar tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        executor.diskIO.execute {

            val lamaran = Lamaran(
                lamaran_id = 0,
                pelamar_id = pelamarId,
                job_id = lowonganId,
                status_lamaran = "Menunggu",
                tanggal_lamaran = System.currentTimeMillis()
            )

            db.lamaranDao().insert(lamaran)

            runOnUiThread {
                Toast.makeText(this, "Lamaran berhasil dikirim", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
