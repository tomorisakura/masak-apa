package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.grevi.masakapa.R
import com.grevi.masakapa.data.local.entity.RecipesTable
import com.grevi.masakapa.databinding.ListMainRecipesBinding
import com.grevi.masakapa.common.differ.Differ

class RecipesAdapter(private val itemTouch : ((recipes : RecipesTable) -> Unit))
    : RecyclerView.Adapter<RecipesAdapter.RecipesVH>() {
    private val recipes : MutableList<RecipesTable> = ArrayList()

    inner class RecipesVH(private val binding : ListMainRecipesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipes : RecipesTable) = with(binding) {
            imgThumb.load(recipes.imageThumb) {
                allowHardware(false)
                crossfade(true)
                placeholder(R.drawable.placeholder)
            }
            recipesTitleMain.text = recipes.name
            dificultyTextMain.text = recipes.dificulty
            portionTextMain.text = recipes.portion
            timesTextMain.text = recipes.times
        }
    }

    fun addItem(item : MutableList<RecipesTable>) {
        val differCallback = Differ(this.recipes, item)
        val diffResult = DiffUtil.calculateDiff(differCallback)
        recipes.clear()
        recipes.addAll(item)
        diffResult.dispatchUpdatesTo(this)
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