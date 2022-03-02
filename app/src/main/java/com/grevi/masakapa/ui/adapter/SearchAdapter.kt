package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ListsRecipesBinding
import com.grevi.masakapa.model.Search
import com.grevi.masakapa.common.differ.Differ

class SearchAdapter(private val itemTouch : ((search : Search) -> Unit)) :
    RecyclerView.Adapter<SearchAdapter.SearchVH>() {
    private val recipes : MutableList<Search> = ArrayList()

    inner class SearchVH(private val binding : ListsRecipesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(search : Search) = with(binding) {
            imgThumb.load(search.imageThumb) {
                allowHardware(false)
                crossfade(true)
                placeholder(R.drawable.placeholder)
            }
            recipesTitle.text = search.name
            dificultyText.text = search.difficulty
            portionText.text = search.servings
            timesText.text = search.times
        }
    }

    fun addItem(item : List<Search>) {
        val diffCallback = Differ(this.recipes, item)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        recipes.clear()
        recipes.addAll(item)
        diffResult.dispatchUpdatesTo(this)
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
        holder.itemView.setOnClickListener { itemTouch.invoke(recipes[position]) }
    }
}