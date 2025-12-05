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
import com.example.kerjain.BookmarkActivity
import com.example.kerjain.R
import com.example.kerjain.data.Bookmark
import com.example.kerjain.ui.adapters.BookmarksAdapter
import com.example.kerjain.ui.viewmodel.MainListViewModel
import com.google.android.material.snackbar.Snackbar

class BookmarksFragment : Fragment(R.layout.activity_bookmark) {

    private val vm: MainListViewModel by activityViewModels()
    private lateinit var adapter: BookmarksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = view.findViewById<RecyclerView>(R.id.rvBookmarks)
        val btnBack = view.findViewById<ImageView>(R.id.btnBackBookmark)
        val btnDeleteAll = view.findViewById<ImageView>(R.id.btnDeleteAllBookmarks)
        val empty = view.findViewById<View>(R.id.emptyState)

        adapter = BookmarksAdapter(mutableListOf(),
            onOpen = { /* navigate to job detail if required */ },
            onRemove = { b -> vm.deleteBookmark(b) }
        )

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        val touch = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.bindingAdapterPosition
                val removed = adapter.removeAt(pos)
                vm.deleteBookmark(removed)
                Snackbar.make(rv, "Bookmark dihapus", Snackbar.LENGTH_LONG)
                    .setAction("Batal") {
                        vm.addBookmark(removed)
                    }.show()
            }
        })
        touch.attachToRecyclerView(rv)

        vm.bookmarks.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            empty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        btnBack.setOnClickListener { requireActivity().onBackPressed() }

        btnDeleteAll.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Hapus Semua Bookmark")
                .setMessage("Yakin ingin menghapus semua bookmark?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ -> vm.clearBookmarks() }
                .show()
        }
    }
}
