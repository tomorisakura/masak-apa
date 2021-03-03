package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ListsRecipesBinding
import com.grevi.masakapa.model.Recipes

class CategoryItemAdapter : RecyclerView.Adapter<CategoryItemAdapter.CategoryItemVH>() {
    private val recipes : MutableList<Recipes> = mutableListOf()
    internal var itemTouch : ((recipes : Recipes) -> Unit)? = null

    inner class CategoryItemVH(private val binding : ListsRecipesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipes : Recipes) {
            with(binding) {
                Glide.with(root).load(recipes.imageThumb).placeholder(R.drawable.placeholder).into(imgThumb)
                recipesTitle.text = recipes.name
                dificultyText.text = recipes.dificulty
                portionText.text = recipes.portion
                timesText.text = recipes.times
            }
        }
    }

    fun addItem(item : MutableList<Recipes>) {
        recipes.clear()
        recipes.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemVH {
        val view = ListsRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryItemVH(view)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: CategoryItemVH, position: Int) {
        holder.bind(recipes[position])
        holder.itemView.setOnClickListener { itemTouch?.invoke(recipes[position]) }
    }
}