package com.example.kerjain.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.AppDatabase
import com.example.kerjain.data.AppExecutor
import com.example.kerjain.data.Chat
import com.example.kerjain.data.Perusahaan
import com.example.kerjain.ui.chat.ChatListAdapter.ChatListItem
import com.example.kerjain.ui.chatroom.ChatRoomActivity

class ChatPelamarFragment : Fragment() {

    private lateinit var rvChats: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout

    private lateinit var db: AppDatabase
    private lateinit var executor: AppExecutor
    private lateinit var adapter: ChatListAdapter

    private val pelamarId: Int
        get() = requireActivity().intent.getIntExtra("PELAMAR_ID", -1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_chat_pelamar, container, false)
        rvChats = root.findViewById(R.id.rvChats)
        emptyStateLayout = root.findViewById(R.id.emptyStateLayout)

        db = AppDatabase.getDatabase(requireContext())
        executor = AppExecutor()

        adapter = ChatListAdapter(emptyList()) { item ->
            val intent = Intent(requireContext(), ChatRoomActivity::class.java).apply {
                putExtra("PELAMAR_ID", pelamarId)
                putExtra("COMPANY_ID", item.companyId)
            }
            startActivity(intent)
        }
        rvChats.layoutManager = LinearLayoutManager(requireContext())
        rvChats.adapter = adapter

        loadChatList()

        return root
    }

    private fun loadChatList() {
        if (pelamarId < 0) {
            Toast.makeText(requireContext(), "Pelamar ID tidak tersedia", Toast.LENGTH_SHORT).show()
            return
        }

        executor.diskIO.execute {
            val all = db.chatDao().getAllForUser(pelamarId)

            val lastMap = LinkedHashMap<Int, Chat>()
            for (c in all) {
                val otherId = if (c.sender_id == pelamarId) c.receiver_id else c.sender_id
                lastMap[otherId] = c
            }

            val list = lastMap.entries.map { entry ->
                val companyId = entry.key
                val lastChat = entry.value
                val company: Perusahaan? = db.perusahaanDao().getById(companyId)
                ChatListItem(companyId = companyId, company = company, lastChat = lastChat)
            }.sortedByDescending { it.lastChat.timestamp } // newest first

            executor.mainThread.execute {
                if (list.isEmpty()) {
                    emptyStateLayout.visibility = View.VISIBLE
                } else {
                    emptyStateLayout.visibility = View.GONE
                }
                adapter.update(list)
            }
        }
    }
}
