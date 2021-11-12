package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.grevi.masakapa.databinding.ListsCategorysBinding
import com.grevi.masakapa.data.local.entity.Category
import com.grevi.masakapa.util.DiffUtils

class CategorysAdapter : RecyclerView.Adapter<CategorysAdapter.CategoryVH>() {

    private val categorys : MutableList<Category> = mutableListOf()
    internal var itemTouch : ((category : Category) -> Unit)? = null

    inner class CategoryVH(private val binding : ListsCategorysBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categorys: Category) = with(binding) {
            categorysText.text = categorys.category
        }
    }

    fun addItem(item : List<Category>) {
        val diffCallback = DiffUtils(categorys, item)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categorys.clear()
        categorys.addAll(item)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view = ListsCategorysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(view)
    }

    override fun getItemCount(): Int {
        return categorys.size
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.bind(categorys[position])
        holder.itemView.setOnClickListener { itemTouch?.invoke(categorys[position]) }
    }
}