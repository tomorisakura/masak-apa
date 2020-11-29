package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.masakapa.R
import com.grevi.masakapa.model.Recipes
import com.grevi.masakapa.util.Listenear
import kotlinx.android.synthetic.main.lists_recipes.view.*

class CategoryItemAdapter : RecyclerView.Adapter<CategoryItemAdapter.CategoryItemVH>() {
    private val recipes : MutableList<Recipes> = mutableListOf()
    private var listenear : Listenear? = null

    inner class CategoryItemVH(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(recipes : Recipes) {
            with(itemView) {
                Glide.with(this.context).load(recipes.imageThumb).placeholder(R.drawable.placeholder).into(imgThumb)
                recipesTitle.text = recipes.name
                dificultyText.text = recipes.dificulty
                portionText.text = recipes.portion
                timesText.text = recipes.times
                this.setOnClickListener { listenear?.onItemSelected(recipes) }
            }
        }
    }

    internal fun itemRecipes(listenear: Listenear) {
        this.listenear = listenear
    }

    fun addItem(item : MutableList<Recipes>) {
        recipes.clear()
        recipes.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lists_recipes, parent, false)
        return CategoryItemVH(view)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: CategoryItemVH, position: Int) {
        holder.bind(recipes[position])
    }
}