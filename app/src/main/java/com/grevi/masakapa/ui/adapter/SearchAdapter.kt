package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.masakapa.R
import com.grevi.masakapa.model.Search
import com.grevi.masakapa.util.SearchListenear
import kotlinx.android.synthetic.main.lists_recipes.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchVH>() {

    private val recipes : MutableList<Search> = mutableListOf()
    private var listenear : SearchListenear? = null

    inner class SearchVH(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(search : Search) {
            with(itemView) {
                Glide.with(this.context).load(search.imageThumb).placeholder(R.drawable.placeholder).into(imgThumb)
                recipesTitle.text = search.name
                dificultyText.text = search.difficulty
                portionText.text = search.servings
                timesText.text = search.times
                this.setOnClickListener { listenear?.onItemSelected(search) }
            }
        }
    }

    internal fun itemRecipes(listenear: SearchListenear) {
        this.listenear = listenear
    }

    fun addItem(item : List<Search>) {
        recipes.clear()
        recipes.addAll(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lists_recipes, parent, false)
        return SearchVH(view)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        holder.bind(recipes[position])
    }
}