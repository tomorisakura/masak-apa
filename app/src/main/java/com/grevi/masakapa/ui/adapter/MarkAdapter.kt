package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ListsRecipesBinding
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.util.DiffUtils

class MarkAdapter : RecyclerView.Adapter<MarkAdapter.MarkVH>() {
    private val recipes : MutableList<RecipesTable> = ArrayList()
    internal var itemTouch : ((recipes : RecipesTable) -> Unit)? = null

    inner class MarkVH(private val binding : ListsRecipesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipesTable : RecipesTable) = with(binding) {
            imgThumb.load(recipesTable.imageThumb) {
                allowHardware(false)
                crossfade(true)
                placeholder(R.drawable.placeholder)
            }
            recipesTitle.text = recipesTable.name
            dificultyText.text = recipesTable.dificulty
            portionText.text = recipesTable.portion
            timesText.text = recipesTable.times
        }
    }

    fun addItem(item : List<RecipesTable>) {
        val differCallback = DiffUtils(this.recipes, item)
        val diffResult = DiffUtil.calculateDiff(differCallback, true)
        recipes.clear()
        recipes.addAll(item)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(position: Int) : Boolean = recipes.remove(recipes[position])

    fun deleteItem(position: Int) : RecipesTable = recipes[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkVH {
        val view = ListsRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarkVH(view)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: MarkVH, position: Int) {
        holder.bind(recipes[position])
        holder.itemView.setOnClickListener { itemTouch?.invoke(recipes[position]) }
    }

}