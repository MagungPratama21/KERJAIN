package com.example.kerjain.ui.chatroom

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.AppExecutor
import com.example.kerjain.data.Chat

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var ivChatProfilePicture: ImageView
    private lateinit var tvChatName: TextView
    private lateinit var tvChatStatus: TextView
    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: FrameLayout

    private lateinit var db: AppDatabase
    private lateinit var executor: AppExecutor
    private lateinit var adapter: ChatRoomAdapter

    private var pelamarId: Int = -1
    private var companyId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        initViews()
        initDatabase()
        readExtras()
        setupRecycler()
        setupListeners()
        observeMessages()
        loadCompanyInfo()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        ivChatProfilePicture = findViewById(R.id.ivChatProfilePicture)
        tvChatName = findViewById(R.id.tvChatName)
        tvChatStatus = findViewById(R.id.tvChatStatus)
        rvMessages = findViewById(R.id.rvMessages)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
    }

    private fun initDatabase() {
        db = AppDatabase.getDatabase(this)
        executor = AppExecutor()
    }

    private fun readExtras() {
        pelamarId = intent.getIntExtra("PELAMAR_ID", -1)
        companyId = intent.getIntExtra("COMPANY_ID", -1)
    }

    private fun setupRecycler() {
        adapter = ChatRoomAdapter(currentUserId = pelamarId)
        rvMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        rvMessages.adapter = adapter
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }

        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (TextUtils.isEmpty(message)) return@setOnClickListener

            val chat = Chat(
                chat_id = 0,
                sender_id = pelamarId,
                receiver_id = companyId,
                message = message,
                timestamp = System.currentTimeMillis()
            )

            executor.diskIO.execute {
                db.chatDao().insert(chat)

                executor.mainThread.execute {
                    etMessage.setText("")
                    rvMessages.post {
                        rvMessages.scrollToPosition(adapter.itemCount - 1)
                    }
                }
            }
        }
    }

    private fun observeMessages() {
        db.chatDao().getChat(pelamarId, companyId).observe(this) { list ->
            if (list == null || list.isEmpty()) {
                adapter.setItems(emptyList())
            } else {
                adapter.setItems(list)
                rvMessages.post {
                    rvMessages.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun loadCompanyInfo() {
        executor.diskIO.execute {
            val company = db.perusahaanDao().getById(companyId)
            executor.mainThread.execute {
                if (company != null) {
                    tvChatName.text = company.nama_perusahaan
                    ivChatProfilePicture.setImageResource(R.drawable.company_placeholder)
                    tvChatStatus.text = "Online"
                }
            }
        }
    }
}
