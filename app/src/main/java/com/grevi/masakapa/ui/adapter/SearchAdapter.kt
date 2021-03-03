package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ListsRecipesBinding
import com.grevi.masakapa.model.Search

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchVH>() {
    private val recipes : MutableList<Search> = mutableListOf()
    internal var itemTouch : ((search : Search) -> Unit)? = null

    inner class SearchVH(private val binding : ListsRecipesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(search : Search) = with(binding) {
            Glide.with(root).load(search.imageThumb).placeholder(R.drawable.placeholder).into(imgThumb)
            recipesTitle.text = search.name
            dificultyText.text = search.difficulty
            portionText.text = search.servings
            timesText.text = search.times
        }
    }

    fun addItem(item : List<Search>) {
        recipes.clear()
        recipes.addAll(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val view = ListsRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchVH(view)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        holder.bind(recipes[position])
        holder.itemView.setOnClickListener { itemTouch?.invoke(recipes[position]) }
    }
}