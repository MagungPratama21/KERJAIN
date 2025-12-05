package com.example.kerjain.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R
import com.example.kerjain.data.NotificationItem
import com.example.kerjain.ui.adapters.NotificationsAdapter
import com.example.kerjain.ui.viewmodel.MainListViewModel
import com.google.android.material.snackbar.Snackbar

class NotificationsFragment : Fragment(R.layout.activity_notifikasi_main) {

    private val vm: MainListViewModel by activityViewModels()
    private lateinit var adapter: NotificationsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = view.findViewById<RecyclerView>(R.id.rvNotifications)
        val btnBack = view.findViewById<ImageView>(R.id.btnBackNotif)
        val btnDeleteAll = view.findViewById<ImageView>(R.id.btnDeleteAllNotifs)
        val empty = view.findViewById<View>(R.id.emptyState)

        adapter = NotificationsAdapter(mutableListOf(), onRemove = { n -> vm.deleteNotification(n) })
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        val touch = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.bindingAdapterPosition
                val removed = adapter.removeAt(pos)
                vm.deleteNotification(removed)
                Snackbar.make(rv, "Notifikasi dihapus", Snackbar.LENGTH_LONG)
                    .setAction("Batal") { vm.addNotification(removed) }
                    .show()
            }
        })
        touch.attachToRecyclerView(rv)

        vm.notifications.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            empty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        btnBack.setOnClickListener { requireActivity().onBackPressed() }

        btnDeleteAll.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Hapus Semua Notifikasi")
                .setMessage("Yakin ingin menghapus semua notifikasi?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ -> vm.clearNotifications() }
                .show()
        }
    }
}
