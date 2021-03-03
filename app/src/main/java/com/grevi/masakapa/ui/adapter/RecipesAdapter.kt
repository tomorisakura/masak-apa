package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ListMainRecipesBinding
import com.grevi.masakapa.model.Recipes

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipesVH>() {
    private val recipes : MutableList<Recipes> = mutableListOf()
    internal var itemTouch : ((recipes : Recipes) -> Unit)? = null

    inner class RecipesVH(private val binding : ListMainRecipesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipes : Recipes) = with(binding) {
            Glide.with(root).load(recipes.imageThumb).placeholder(R.drawable.placeholder).into(imgThumb)
            recipesTitleMain.text = recipes.name
            dificultyTextMain.text = recipes.dificulty
            portionTextMain.text = recipes.portion
            timesTextMain.text = recipes.times
        }
    }

    fun addItem(item : MutableList<Recipes>) {
        notifyDataSetChanged()
        recipes.clear()
        recipes.addAll(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesVH {
        val view = ListMainRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipesVH(view)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecipesVH, position: Int) {
        holder.bind(recipes[position])
        holder.itemView.setOnClickListener { itemTouch?.invoke(recipes[position]) }
    }
}