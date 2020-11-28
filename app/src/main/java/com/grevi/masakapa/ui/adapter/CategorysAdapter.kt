package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grevi.masakapa.R
import com.grevi.masakapa.model.Categorys
import com.grevi.masakapa.util.CategoryListenear
import kotlinx.android.synthetic.main.lists_categorys.view.*

class CategorysAdapter : RecyclerView.Adapter<CategorysAdapter.CategoryVH>() {

    private val categorys : MutableList<Categorys> = mutableListOf()
    private var listenear : CategoryListenear? = null

    inner class CategoryVH(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(categorys: Categorys) {
            itemView.categorysText.text = categorys.category
            itemView.setOnClickListener { listenear?.onItemSelected(categorys) }
        }
    }

    internal fun itemCategory(listenear : CategoryListenear) {
        this.listenear = listenear
    }

    fun addItem(item : List<Categorys>) {
        categorys.clear()
        categorys.addAll(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lists_categorys, parent, false)
        return CategoryVH(view)
    }

    override fun getItemCount(): Int {
        return categorys.size
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.bind(categorys[position])
    }
}