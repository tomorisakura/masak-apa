package com.grevi.masakapa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import coil.load
import com.grevi.masakapa.R
import com.grevi.masakapa.data.local.entity.RecipeFavorite
import com.grevi.masakapa.databinding.ListsRecipesBinding
import com.grevi.masakapa.common.differ.Differ

class MarkAdapter(private val itemTouch : ((favorite : RecipeFavorite) -> Unit)) : RecyclerView.Adapter<MarkAdapter.MarkVH>() {
    private val recipes : MutableList<RecipeFavorite> = ArrayList()

    inner class MarkVH(private val binding : ListsRecipesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite : RecipeFavorite) = with(binding) {
            imgThumb.load(favorite.imageThumb) {
                allowHardware(false)
                crossfade(true)
                placeholder(R.drawable.placeholder)
            }
            recipesTitle.text = favorite.name
            dificultyText.text = favorite.dificulty
            portionText.text = favorite.portion
            timesText.text = favorite.times
        }
    }

    fun addItem(item : List<RecipeFavorite>) {
        val diffResult = DiffUtil.calculateDiff(Differ(recipes, item), true)
        recipes.clear()
        recipes.addAll(item)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(position: Int) : Boolean {
        return recipes.remove(recipes[position])
    }

    fun deleteItem(position: Int) : RecipeFavorite = recipes[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkVH {
        val view = ListsRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarkVH(view)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: MarkVH, position: Int) {
        holder.bind(recipes[position])
        holder.itemView.setOnClickListener {
            /*
            * filtering position when clicked item
            * */
            val adapterPosition = holder.adapterPosition.takeIf { it != NO_POSITION } ?: return@setOnClickListener
            itemTouch(recipes[adapterPosition])
        }
    }

}