package com.example.kerjain.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.kerjain.R

class CategoryAdapter(
    private val items: List<String>,
    private val maxSelected: Int = 3,
    private val onSelectionChanged: (selected: List<String>) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.VH>() {

    private val selected = mutableSetOf<String>()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbCategory: CheckBox = itemView.findViewById(R.id.cbCategory)

        init {
            cbCategory.setOnClickListener {
                val cat = cbCategory.text.toString()
                if (cbCategory.isChecked) {
                    if (selected.size >= maxSelected) {
                        cbCategory.isChecked = false
                    } else {
                        selected.add(cat)
                        onSelectionChanged(selected.toList())
                    }
                } else {
                    selected.remove(cat)
                    onSelectionChanged(selected.toList())
                }
            }

            itemView.setOnClickListener {
                cbCategory.performClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job_kategory, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val cat = items[position]
        holder.cbCategory.text = cat
        holder.cbCategory.isChecked = selected.contains(cat)
    }

    override fun getItemCount(): Int = items.size

    fun clearSelection() {
        selected.clear()
        notifyDataSetChanged()
        onSelectionChanged(selected.toList())
    }

    fun getSelected(): List<String> = selected.toList()
}
