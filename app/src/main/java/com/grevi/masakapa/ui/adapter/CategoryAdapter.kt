package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.grevi.masakapa.common.differ.Differ
import com.grevi.masakapa.data.local.entity.Category
import com.grevi.masakapa.databinding.ListsCategorysBinding
import com.grevi.masakapa.model.Categorys

class CategoryAdapter(private val itemTouch: ((category: Categorys) -> Unit)) :
    RecyclerView.Adapter<CategoryAdapter.CategoryVH>() {

    private val category: MutableList<Categorys> = mutableListOf()

    inner class CategoryVH(private val binding: ListsCategorysBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Categorys) = with(binding) {
            categorysText.text = category.category
        }
    }

    fun addItem(item: List<Categorys>) {
        val diffCallback = Differ(category, item)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        category.clear()
        category.addAll(item)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view = ListsCategorysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(view)
    }

    override fun getItemCount(): Int {
        return category.size
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.bind(category[position])
        holder.itemView.setOnClickListener { itemTouch.invoke(category[position]) }
    }
}