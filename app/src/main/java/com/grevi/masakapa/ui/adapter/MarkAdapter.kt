package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.ListsRecipesBinding
import com.grevi.masakapa.db.entity.RecipesTable

class MarkAdapter : RecyclerView.Adapter<MarkAdapter.MarkVH>() {
    private val recipes : MutableList<RecipesTable> = mutableListOf()
    internal var itemTouch : ((recipes : RecipesTable) -> Unit)? = null

    inner class MarkVH(private val binding : ListsRecipesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipesTable : RecipesTable) = with(binding) {
            Glide.with(root).load(recipesTable.imageThumb).placeholder(R.drawable.placeholder).into(imgThumb)
            recipesTitle.text = recipesTable.name
            dificultyText.text = recipesTable.dificulty
            portionText.text = recipesTable.portion
            timesText.text = recipesTable.times
        }
    }

    fun addItem(item : List<RecipesTable>) {
        recipes.clear()
        recipes.addAll(item)
    }

    fun restoreItem(item: RecipesTable, position: Int) {
        recipes.add(item)
        notifyItemInserted(position)
    }

    fun removeItem(item: RecipesTable, position: Int) {
        recipes.remove(item)
        notifyItemRemoved(position)
        notifyItemChanged(position)
        notifyDataSetChanged()
    }

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